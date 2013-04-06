package com.softserve.cart.model

import com.softserve.cart.model._
import com.softserve.cart.commands._
import com.softserve.cart.infrastructure.{Db, Repository}
import org.scalatra.commands._
import org.scalatra.validation._
import scala.util.control.Exception._
import scalaz._
import Scalaz._

import org.squeryl._
import org.squeryl.dsl._
import org.squeryl.PrimitiveTypeMode._

object ProductRepository extends Repository[Long, Product]{
  def relation = Db.products
}

object ScalazGoodies {
  import scala.language.implicitConversions
  trait ValidatedField[T]{
    def field: Field[T]

    def toValidation: ValidationNEL[ValidationError, T] = field.value.toSuccess {
        ValidationError(field.error.getOrElse("Unknown error at %s" format field.name).toString).wrapNel
    }
  }
  implicit def ValidationTo[T](_field: Field[T]): ValidatedField[T] = new ValidatedField[T] {
    val field = _field
  }
}
object CartRepository extends Repository[CompositeKey2[Long, String], CartItem] with CommandHandler {
  import org.squeryl.PrimitiveTypeMode._
  import ScalazGoodies._
  lazy val relation = Db.cartItems

  def cart(cartId: String) = from(relation)(s => where(s.cartId === cartId) select(s))

  def sum(cartId: String): Int =
    // In real world I would do it in DB, but doing it in Scala is much more fun
    cart(cartId).map(_.sum).foldLeft(0)(_ + _)

  def newId = java.util.UUID.randomUUID.toString

  private def applyAdd(c: AddToCartCommand): ModelValidation[CartItem] = {
      for {
        productId <- c.itemId.toValidation
        cartId <- c.cartId.toValidation
        amount <- c.amount.toValidation
        product <- ProductRepository.lookup(productId).toSuccess(ValidationError("Product not found").wrapNel)
      } yield insertOrAdd(cartId, product, amount)
  }
  private def applyRemove(c: RemoveFromCartCommand): ModelValidation[Int] = {
    for {
      productId <- c.productId.toValidation
      cartId <- c.cartId.toValidation
    } yield Db.cartItems.deleteWhere(s => s.productId === productId and s.cartId === cartId)
  }
  private def applyCheckout(c: CheckoutCommand): ModelValidation[Int] = {
    for {
      cartId <- c.cartId.toValidation
    } yield Db.cartItems.deleteWhere(_.cartId === cartId)
  }

  protected def handle: Handler = {
    case c: AddToCartCommand => applyAdd(c)
    case c: RemoveFromCartCommand => applyRemove(c)
    case c: CheckoutCommand => applyCheckout(c)
    }
  private def insertOrAdd(cartId: String, p: Product, count: Int): CartItem = {
    from(relation)(s => where(s.productId === p.id) select(s)).headOption.
      map { loaded =>
          val updated = loaded.copy(count = loaded.count + count)
          Db.cartItems.update(updated)
          updated
      } getOrElse {
          val created = CartItem(p.id, cartId, count)
          Db.cartItems.insert(created)
          created
      }
  }
}


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

object CartRepository extends Repository[CompositeKey2[Long, String], CartItem] with CommandHandler {
  import org.squeryl.PrimitiveTypeMode._
  lazy val relation = Db.cartItems

  def cart(cartId: String) = from(relation)(s => where(s.cartId === cartId) select(s))

  def sum(cartId: String): Int =
    // In real world I would do it in DB, but doing it in Scala is much more fun
    cart(cartId).map(_.sum).foldLeft(0)(_ + _)

  def newId = java.util.UUID.randomUUID.toString

  protected def handle: Handler = {
    case c: AddToCartCommand => allCatch.withApply(errorFail) {
        // TODO I beliebe some more functional apporoach exists
        // to transform from Option to NEL
        val product = ProductRepository.lookup(c.itemId.value.get).get
        add(c.cartId.value.get, product, c.amount.value.get)
    }
    case c: RemoveFromCartCommand => allCatch.withApply(errorFail) {
        val productId = c.productId.value.get
        Db.cartItems.deleteWhere(s => s.productId === productId and s.cartId === c.cartId.value.get).
          successNel : ModelValidation[Int]
    }
    case c: CheckoutCommand => allCatch.withApply(errorFail) {
        Db.cartItems.deleteWhere(_.cartId === c.cartId.value.get).
          successNel : ModelValidation[Int]
      }
    }
  private def add(cartId: String, p: Product, count: Int): ModelValidation[CartItem] = {
    val ci: CartItem = from(relation)(s => where(s.productId === p.id) select(s)).headOption.
      map { loaded =>
          val updated = loaded.copy(count = loaded.count + count)
          Db.cartItems.update(updated)
          updated
      } getOrElse {
          val created = CartItem(p.id, cartId, count)
          Db.cartItems.insert(created)
          created
      }
      ci.successNel
  }
  def errorFail(ex: Throwable) = ValidationError(ex.getMessage, UnknownError).failNel
}


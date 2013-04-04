package com.softserve.cart.model

import com.softserve.cart.model._
import com.softserve.cart.commands._
import com.softserve.cart.infrastructure.{Db, Repository}
import org.scalatra.commands._
import org.scalatra.validation._
import scala.util.control.Exception._
import scalaz._
import Scalaz._

object ProductRepository extends Repository[Product]{
  def relation = Db.products
}

object UserRepository extends Repository[User] with CommandHandler {
  import org.squeryl.PrimitiveTypeMode._
  lazy val relation = Db.users

  def login(userName: String, password: String) = inTransaction {
    from(relation)(s => where(s.name === userName and s.password === password)
      select(s)).headOption
  }

  def cart(user: User) = inTransaction {
    user.cart.associationMap.toList
  }

  protected def handle: Handler = {
    case c: AddToCartCommand => transaction {
      allCatch.withApply(errorFail) {
        // TODO I beliebe some more functional apporoach exists
        val product = ProductRepository.lookup(c.itemId.value.get).get
        add(c.user, product, c.amount.value.get)
      }
    }
    case c: RemoveFromCartCommand => transaction {
      allCatch.withApply(errorFail) {
        val productId = c.productId.value.get
        Db.cartItems.deleteWhere(s => s.productId === productId and s.userId === c.user.id)
        ().successNel : ModelValidation[Unit]
      }
    }
  }
  private def add(u: User, p: Product, count: Int): ModelValidation[CartItem] = {
      val ci = u.cart.associations.where(_.productId === p.id).headOption match {
        case Some(loaded) => {
          val updated = loaded.copy(count = loaded.count + count)
          Db.cartItems.update(updated)
          updated
        }
        case None => {
          val created = u.cart.assign(p).copy(count = count)
          Db.cartItems.insert(created)
          created
        }
      }
      ci.successNel
  }
  def errorFail(ex: Throwable) = ValidationError(ex.getMessage, UnknownError).failNel
}


package com.softserve.cart.model

import com.softserve.cart.model._
import com.softserve.cart.infrastructure.{Db, Repository}

object ProductRepository extends Repository[Product] {
  def relation = Db.products
}

object UserRepository extends Repository[User] {
  import org.squeryl.PrimitiveTypeMode._
  lazy val relation = Db.users

  def login(userName: String, password: String) = inTransaction {
    from(relation)(s => where(s.name === userName and s.password === password)
      select(s)).headOption
  }

  def cart(user: User) = inTransaction {
    user.cart.toList
  }

}


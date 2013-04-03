package com.softserve.cart.model

import com.softserve.cart.model._
import com.softserve.cart.infrastructure.{Db, Repository}

object CartRepository extends Repository[Item] {
  def relation = Db.items
}

object UserRepository extends Repository[User] {
  import org.squeryl.PrimitiveTypeMode._
  def relation = Db.users

  def login(userName: String, password: String) = inTransaction {
    from(relation)(s => where(s.name === userName and s.password === password) select(s)).headOption
  }

  def cartItems(userId: Long) = inTransaction {
    from(Db.cartItems, Db.items)((ci, i) => where(ci.userId === userId and ci.itemId === i.id) select((ci, i)))
  }
}


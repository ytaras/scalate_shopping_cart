package com.softserve.cart.repository

import com.softserve.cart.model._
import org.squeryl.{Schema,Table}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.KeyedEntity

object CartRepository extends Repository[Item] {
  def relation = Db.items
}

object UserRepository extends Repository[User] {
  def relation = Db.users

  def login(userName: String, password: String) = inTransaction {
    from(relation)(s => where(s.name === userName and s.password === password) select(s)).headOption
  }
}

trait Repository[Entity <: KeyedEntity[Long]] {
  def relation: Table[Entity]
  def all = transaction {
    from(relation)(select(_)).toIndexedSeq
  }
  def lookup(key: Long) = relation.lookup(key)
}

object Db extends Schema {
  import org.squeryl.PrimitiveTypeMode._
  val items = table[Item]
  val users = table[User]

  def importDefaultDataset = transaction {
    create
    items.insert(Item(1, "item1", "description1", 10))
    users.insert(User(1, "cart", "cart"))
  }
}

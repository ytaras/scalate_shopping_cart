package com.softserve.cart.model

import org.squeryl._
import org.squeryl.dsl._
import org.squeryl.PrimitiveTypeMode._
import com.softserve.cart.infrastructure.Db

case class Item(id: Long, name: String, description: String, price: Int)
  extends KeyedEntity[Long]

// TODO Storing passwords in plain text is insecure, still this is a PoC only
case class User(id: Long, name: String, password: String)
  extends KeyedEntity[Long] {
  lazy val cart: Query[(Item, CartItem)] = Db.cartItems.right(this).associationMap
}
case class CartItem(itemId: Long, userId: Long, count: Int)
  extends KeyedEntity[CompositeKey2[Long, Long]] {
  def id = compositeKey(itemId, userId)
  def stringId = itemId + "_" + userId

  // TODO Find out what is correct pattern for using transactions and lazy load in Squeryl
  // so I won't have to pass item as a parameter
  def sum(item: Item) = item.price * count
}

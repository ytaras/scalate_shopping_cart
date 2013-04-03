package com.softserve.cart.model

import org.squeryl.KeyedEntity

case class Item(id: Long, name: String, description: String, price: Int) extends KeyedEntity[Long]

// TODO Storing passwords in plain text is insecure, still this is a PoC only
case class User(id: Long, name: String, password: String) extends KeyedEntity[Long] {
  import com.softserve.cart.model.UserRepository
  def cartItems = UserRepository.cartItems(id)
}
case class CartItem(id: Long, itemId: Long, userId: Long, count: Int) extends KeyedEntity[Long]

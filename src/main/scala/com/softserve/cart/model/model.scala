package com.softserve.cart.model

import org.squeryl._
import org.squeryl.dsl._
import org.squeryl.PrimitiveTypeMode._
import com.softserve.cart.infrastructure.Db

case class Product(id: Long, name: String, description: String, price: Int)
  extends KeyedEntity[Long]

case class CartItem(productId: Long, cartId: String, count: Int)
  extends KeyedEntity[CompositeKey2[Long, String]] {
  def id = compositeKey(productId, cartId)
  // Let's fail fast - cartItem without assocciated product is an exception
  lazy val product = ProductRepository.lookup(productId).get

  def sum = product.price * count
}

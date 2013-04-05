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

  // TODO Find out what is correct pattern for using transactions
  // and lazy load in Squeryl
  // so I won't have to pass item as a parameter
  def sum(product: Product) = product.price * count
}

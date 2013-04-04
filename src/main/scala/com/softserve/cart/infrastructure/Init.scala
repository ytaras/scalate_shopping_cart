package com.softserve.cart.infrastructure

import com.softserve.cart.model._

// Utility method to init db. In production some smarter appoach is to be
// chosen, but as quick and dirty solution this is ok
object Init extends App with DbInit {
  configureDb
  connection.bindToCurrentThread
  Db.create

  Db.items.insert(Item(1, "item1", "description1", 10))
  Db.users.insert(User(1, "cart", "cart"))
  Db.cartItems.insert(CartItem(1, 1, 5))
}

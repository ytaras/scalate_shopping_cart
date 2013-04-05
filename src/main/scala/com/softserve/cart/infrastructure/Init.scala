package com.softserve.cart.infrastructure

import com.softserve.cart.model._

// Utility method to init db. In production some smarter appoach is to be
// chosen, but as quick and dirty solution this is ok
object Init extends App with DbInit {
  configureDb
  connection.bindToCurrentThread
  Db.create

  (0 until 10).foreach { x =>
     Db.products.insert(Product(x, "item" + x, "description" + x, 17 * x))
   }
}

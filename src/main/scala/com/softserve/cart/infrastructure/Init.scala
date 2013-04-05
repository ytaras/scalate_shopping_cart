package com.softserve.cart.infrastructure

import com.softserve.cart.model._
import scala.util.Random

// Utility method to init db. In production some smarter appoach is to be
// chosen, but as quick and dirty solution this is ok
object Init extends App with DbInit {
  configureDb
  connection.bindToCurrentThread
  Db.create

  val rnd = new Random

  (0 until 10).foreach { x =>
     Db.products.insert(Product(x, "item" + x, "description" + x, rnd.nextInt(50) + 1))
   }
}

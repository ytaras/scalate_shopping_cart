package com.softserve.cart

import org.scalatra._
import scalate.ScalateSupport
import com.softserve.cart.repository.CartRepository

class MyScalatraServlet extends ScalatraShoppingCartStack {
  before(true) {
    contentType="text/html"
  }

  get("/") {
    jade("/index")
  }

  get("/shopping-cart") {
    jade("/shopping-cart")
  }

  get("/items") {
    val items = CartRepository.items

    jade("items", "items" -> items)
  }

}

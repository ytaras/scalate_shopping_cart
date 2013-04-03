package com.softserve.cart

import org.scalatra._
import scalate.ScalateSupport
import com.softserve.cart.model.CartRepository

class MyScalatraServlet extends ScalatraShoppingCartStack {
  before(true) {
    contentType="text/html"
  }

  before("/*") {
    basicAuth
  }

  get("/") {
    jade("/index")
  }

  get("/shopping-cart") {
    jade("/shopping-cart")
  }

  get("/items") {
    val items = CartRepository.all

    jade("items", "items" -> items)
  }

}

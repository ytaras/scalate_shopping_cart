package com.softserve.cart

import org.scalatra._
import scalate.ScalateSupport
import com.softserve.cart.model.CartRepository
import com.softserve.cart.model.UserRepository

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
    jade("/shopping-cart", "cart" -> UserRepository.cart(user))
  }

  get("/items") {
    jade("items", "items" -> CartRepository.all)
  }

}

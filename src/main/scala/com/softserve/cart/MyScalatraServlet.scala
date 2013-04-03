package com.softserve.cart

import org.scalatra._
import scalate.ScalateSupport
import com.softserve.cart.repository.CardRepository

class MyScalatraServlet extends ScalatraShoppingCartStack {
  before(true) {
    contentType="text/html"
  }

  get("/") {
    jade("/index")
  }

  get("/shopping-cart") {
    val hello = CardRepository.hello

    jade("/hello-scalate")
  }

}

package com.softserve.cart

import org.scalatra._
import scalate.ScalateSupport

class MyScalatraServlet extends ScalatraShoppingCartStack {
  before(true) {
    contentType="text/html"
  }

  get("/") {
    jade("/index")
  }

}

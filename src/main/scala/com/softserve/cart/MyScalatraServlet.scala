package com.softserve.cart

import org.scalatra._
import scalate.ScalateSupport
import com.softserve.cart.model._

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

  get("/products") {
    jade("products", "products" -> ProductRepository.all)
  }

  get("/products/:id") {
    ProductRepository.lookup(params("id").toLong) match {
      case Some(product) => jade("/product", "product" -> product)
      case None => NotFound
    }
  }

}

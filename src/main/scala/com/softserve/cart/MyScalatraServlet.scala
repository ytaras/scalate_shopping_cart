package com.softserve.cart

import org.scalatra._
import org.scalatra.commands._
import scalate.ScalateSupport
import com.softserve.cart.model._
import com.softserve.cart.commands._

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
      case None => NotFound("Item with id " + params("id") + " not found")
    }
  }
  post("/shopping-cart") {
    val cmd = command[AddToCartCommand]
    cmd.user = user
    UserRepository.execute(cmd).fold(
      errors => halt(400, errors),
      cart => {
        flash("alert") = "Item added"
        redirect("/shopping-cart")
      }
    )
  }
  post("/shopping-cart/checkout") {
    val cmd = command[CheckoutCommand]
    cmd.user = user
    UserRepository.execute(cmd).fold(
      errors => halt(400, errors),
      cart => {
        flash("alert") = "Cart checked out"
        redirect("/shopping-cart")
      }
    )
  }
  delete("/cartitems/:productId") {
    val cmd = command[RemoveFromCartCommand]
    cmd.user = user
    UserRepository.execute(cmd).fold(
      errors => halt(400, errors),
      _ => {
        flash("alert") = "Cart item removed"
        Ok("Ok")
      }
    )
  }

}

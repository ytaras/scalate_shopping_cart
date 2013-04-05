package com.softserve.cart

import org.scalatra._
import org.scalatra.commands._
import scalate.ScalateSupport
import com.softserve.cart.model._
import com.softserve.cart.commands._

class MyScalatraServlet extends ScalatraShoppingCartStack {
  before(true) {
    contentType="text/html"
    if(cookies.get("shoppingcart_id").isEmpty)
      cookies += ("shoppingcart_id" -> CartRepository.newId)
  }

  get("/") {
    jade("/index")
  }

  get("/shopping-cart") {
    val cart = cookies.get("shoppingcart_id").map { CartRepository.cart(_) } getOrElse Nil
    jade("/shopping-cart", "cart" -> cart, "cookie" -> cookies("shoppingcart_id"))
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
    CartRepository.execute(cmd).fold(
      errors => halt(400, errors),
      cart => {
        flash("alert") = "Item added"
        redirect("/shopping-cart")
      }
    )
  }
  post("/shopping-cart/checkout") {
    val cmd = command[CheckoutCommand]
    CartRepository.execute(cmd).fold(
      errors => halt(400, errors),
      cart => {
        flash("alert") = "Cart checked out"
        redirect("/shopping-cart")
      }
    )
  }
  delete("/cartitems/:productId") {
    val cmd = command[RemoveFromCartCommand]
    CartRepository.execute(cmd).fold(
      errors => halt(400, errors),
      _ => {
        flash("alert") = "Cart item removed"
        Ok("Ok")
      }
    )
  }

  def generateCookie = java.util.UUID.randomUUID.toString

}

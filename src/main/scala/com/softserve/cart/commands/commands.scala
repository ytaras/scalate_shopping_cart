package com.softserve.cart.commands

import org.scalatra.commands._
import org.scalatra.validation._
import com.softserve.cart.model._

class AddToCartCommand extends CartCommands[CartItem] {
  val amount: Field[Int] = asType[Int]("amount").required.greaterThan(0)
  val itemId: Field[Long] = asType[Long]("productId").required.greaterThan(0)
}

class RemoveFromCartCommand extends CartCommands[Int] {
  val productId: Field[Long] = asType[Long]("productId").required.greaterThan(0)
}

class CheckoutCommand extends CartCommands[Int] {
}

abstract class CartCommands[S](implicit mf: Manifest[S]) extends ModelCommand[S] with ParamsOnlyCommand {
  // TODO Figure out how to pass it other way
  var user: User = _
}


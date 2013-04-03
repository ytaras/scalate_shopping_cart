package com.softserve.cart.infrastructure

import org.scalatra.auth._
import org.scalatra.auth.strategy._
import org.scalatra.ScalatraBase
import com.softserve.cart.model._

class MyBasicAuthStrategy(protected override val app: ScalatraBase, realm: String)
  extends BasicAuthStrategy[User](app, realm) {

    protected def validate(userName: String, password: String): Option[User] = {
      if(userName == "cart" && password == "cart")
        Some(User(1, userName, password))
      else
        None
    }

    protected def getUserId(user: User) = user.id.toString
}

trait AuthenticationSupport extends ScentrySupport[User] with BasicAuthSupport[User] {
  self: ScalatraBase =>
  val realm = "Cart realm"

  protected def fromSession = { case id: String => User(1, id, id) }
  protected def toSession = { case u: User => u.id.toString }
  protected val scentryConfig = (new ScentryConfig{}).asInstanceOf[ScentryConfiguration]
  override protected def configureScentry = {
    scentry.unauthenticated {
      scentry.strategies("Basic").unauthenticated
    }
  }

  override protected def registerAuthStrategies = {
    scentry.register("Basic", new MyBasicAuthStrategy(_, realm))
  }
}

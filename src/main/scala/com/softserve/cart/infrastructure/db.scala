package com.softserve.cart.infrastructure
import com.mchange.v2.c3p0._
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.H2Adapter
import org.scalatra.ScalatraBase

trait DbInit {
  val connectionString = "jdbc:mysql://localhost/shopping_cart"

  var cpds = new ComboPooledDataSource
  def configureDb {
    cpds.setDriverClass("com.mysql.jdbc.Driver")
    cpds.setUser("cart")
    cpds.setPassword("cart")
    cpds.setJdbcUrl(connectionString)
    cpds.setMinPoolSize(1)
    cpds.setMaxPoolSize(10)

    SessionFactory.concreteFactory = Some(() => connection)

  }

  def connection = {
    Session.create(cpds.getConnection, new H2Adapter)
  }

  def closeConnection = {
    cpds.close()
  }
}

trait Repository[T, Entity <: KeyedEntity[T]] {
  def relation: Table[Entity]
  def all = from(relation)(select(_))
  def lookup(key: T) = relation.lookup(key)
}

object Db extends Schema {
  import com.softserve.cart.model._
  val products = table[Product]
  val cartItems = table[CartItem]
}

trait DbSessionSupport { this: ScalatraBase =>
  def dbSession = request.get(key).orNull.asInstanceOf[Session]
  val key = getClass.getName

  before(true) {
    request(key) = SessionFactory.newSession
    dbSession.bindToCurrentThread
  }

  after(true) {
    dbSession.close
    dbSession.unbindFromCurrentThread
  }
}

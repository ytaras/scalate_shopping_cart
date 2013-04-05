package com.softserve.cart.infrastructure
import com.mchange.v2.c3p0._
import org.squeryl._
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.adapters.H2Adapter

trait DbInit {
  val connectionString = "jdbc:h2:./test"

  var cpds = new ComboPooledDataSource
  def configureDb {
    cpds.setDriverClass("org.h2.Driver")
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
  def all = inTransaction {
    from(relation)(select(_)).toIndexedSeq
  }
  def lookup(key: T) = inTransaction {
    relation.lookup(key)
  }
}

object Db extends Schema {
  import com.softserve.cart.model._
  val products = table[Product]
  val cartItems = table[CartItem]
}

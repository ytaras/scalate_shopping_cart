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

trait Repository[Entity <: KeyedEntity[Long]] {
  def relation: Table[Entity]
  def all = transaction {
    from(relation)(select(_)).toIndexedSeq
  }
  def lookup(key: Long) = relation.lookup(key)
}

object Db extends Schema {
  import com.softserve.cart.model._
  val items = table[Item]
  val users = table[User]
  val cartItems = table[CartItem]

  def importDefaultDataset = transaction {
    create
    items.insert(Item(1, "item1", "description1", 10))
    users.insert(User(1, "cart", "cart"))
    cartItems.insert(CartItem(1, 1, 1, 5))
  }
}

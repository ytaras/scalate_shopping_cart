package com.softserve.cart.repository

import org.squeryl.{SessionFactory, Session}
import com.softserve.cart.model._
import org.squeryl.Schema

object CartRepository {
  import org.squeryl.adapters.H2Adapter
  import org.squeryl.PrimitiveTypeMode._
  import Db._

  Class.forName("org.h2.Driver")
  SessionFactory.concreteFactory = Some(() => Session.create(
      java.sql.DriverManager.getConnection("jdbc:h2:./test"),
      new H2Adapter()))

  def importDefaultDataset = transaction {
    create
    items.insert(Item(1, "item1", "description1", 10))
  }

  def allItems = transaction {
    from(items) (select(_))
  }
}

object Db extends Schema {
  val items = table[Item]
}

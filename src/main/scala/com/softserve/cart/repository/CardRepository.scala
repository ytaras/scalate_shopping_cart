package com.softserve.cart.repository

import org.squeryl.{SessionFactory, Session}

object CardRepository {
  import org.squeryl.adapters.H2Adapter
  import org.squeryl.PrimitiveTypeMode._

  Class.forName("org.h2.Driver")
  SessionFactory.concreteFactory = Some(() => Session.create(
      java.sql.DriverManager.getConnection("jdbc:h2:./test"),
      new H2Adapter()))

  def hello = transaction {
    
  }
}

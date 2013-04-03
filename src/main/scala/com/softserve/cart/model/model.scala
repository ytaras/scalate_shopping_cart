package com.softserve.cart.model

import org.squeryl.KeyedEntity

case class Item(id: Long, name: String, description: String, price: Int) extends KeyedEntity[Long]


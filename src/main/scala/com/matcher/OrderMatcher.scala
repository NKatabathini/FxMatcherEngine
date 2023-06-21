package com.matcher


import com.github.tototoshi.csv.CSVReader
import com.matcher.model.Order

import java.io.File
import scala.{+:, ::}


object OrderMatcher {

  var orderBook : List[Order] = List.empty

  def main(args: Array[String]): Unit = {



    val csvFile = new File("src/main/resources/exampleOrders.csv")
    val reader = CSVReader.open(csvFile)
    val rows = reader.all()
    reader.close()

    rows.foreach { row =>
      val orderId = row(0).toInt
      val userName = row(1)
      val orderTime = row(2).toLong
      val orderType = row(3)
      val orderQty = row(4).toInt
      val orderPrice = row(5).toFloat

      val order = Order(orderId, userName, orderTime,orderType,orderQty, orderPrice)

      orderBook = orderBook :+ order



    }

//    orderBook.foreach(println)
    orderBook.map(processOrder(_))
//    orderBook.foreach(println)

  }

  def processOrder(order: Order): Unit = {
    val matchingOrders = orderBook.filter { existingOrder =>
      existingOrder.order_qty == order.order_qty && existingOrder.order_type != order.order_type
    }

//    println(s"orderid : ${order.order_id} , order_type : ${order.order_type} " )
//    matchingOrders.foreach(println)

    if (matchingOrders.nonEmpty) {
//      val matchedOrder = matchingOrders.head
//      executeTrade(order, matchedOrder)
//      orderBook = orderBook.filterNot(existingOrder => existingOrder == matchedOrder || existingOrder == order)
//    } else {
      val bestPriceOrder = findBestPriceOrder(order.order_type, matchingOrders)
      executeTrade(order, bestPriceOrder.get)
      orderBook = orderBook.filterNot(existingOrder => existingOrder == bestPriceOrder.get || existingOrder == order)
//      println("-------------------------------")
//      bestPriceOrder.foreach(println)
//      println("-------------------------------")
//      orderBook.foreach(println)

    }
  }

  def executeTrade(order1: Order, order2: Order): Unit = {
    val matchPrice = order1.order_price
    val matchQuantity = order1.order_qty
    val matchTime = System.currentTimeMillis()
    val matchDetails = s"Order ${order2.order_id} matched with ${order1.order_id} time $matchTime, quantity $matchQuantity,at price $matchPrice"
    println(matchDetails)
//    orderBook.filterNot(ordr => ordr.order_id == order1.order_id || ordr.order_id == order2.order_id).foreach(println)
  }

  def findBestPriceOrder(orderType: String, matchingOrders : List[Order]): Option[Order] = {
//    val matchingOrders = orderBook.filter(_.order_type != orderType)
    if (matchingOrders.nonEmpty) {
      Some(
        if (orderType == "BUY") matchingOrders.minBy(_.order_price)
        else matchingOrders.maxBy(_.order_price)
      )
    } else {
      None
    }
  }

}

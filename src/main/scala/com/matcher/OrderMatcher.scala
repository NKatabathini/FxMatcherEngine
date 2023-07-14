package com.matcher


import com.github.tototoshi.csv.{CSVReader, CSVWriter}
import com.matcher.model.Order

import java.io.File
import scala.{+:, ::}


object OrderMatcher {

  var orderBook : List[Order] = List.empty
  var outputOrders : String = "outputOrders.csv"

  def main(args: Array[String]): Unit = {


    //reading CSV file and generating an orderBook
    if(args.nonEmpty)
      readCSVFile(args(0))
      else
    readCSVFile("src/main/resources/exampleOrders.csv")

    if(args.length >= 2)
    outputOrders = args(1)



    // processing order from the orderBook by sequential order
    if(orderBook.nonEmpty)
    orderBook.map(processOrder(_))
    else
      printf("order book is empty")

  }

  def processOrder(order: Order): Unit = {

    // filtering  orders which are matching on order quantity and opposite order type
    val matchingOrders = orderBook.filter { existingOrder =>
      existingOrder.order_qty == order.order_qty && existingOrder.order_type != order.order_type
    }


    if (matchingOrders.nonEmpty) {
//      val matchedOrder = matchingOrders.head
//      executeTrade(order, matchedOrder)
//      orderBook = orderBook.filterNot(existingOrder => existingOrder == matchedOrder || existingOrder == order)
//    } else {
      //finding best price order for the given order type
      val bestPriceOrder = findBestPriceOrder(order.order_type, matchingOrders)
      if(bestPriceOrder.nonEmpty) {
//        executing the order
        executeTrade(order, bestPriceOrder.get)
//        removing orders which are executed from the orderBook
        orderBook = orderBook.filterNot(existingOrder => existingOrder == bestPriceOrder.get || existingOrder == order)
      }

    }
  }

  def executeTrade(order1: Order, order2: Order): Unit = {
    val matchPrice = order1.order_price
    val matchQuantity = order1.order_qty
    val matchTime = System.currentTimeMillis()
    val matchDetails = s"Order ${order2.order_id} matched with ${order1.order_id} time $matchTime, quantity $matchQuantity,at price $matchPrice"
    println(matchDetails)
    writeToCSV(outputOrders, Seq(order2.order_id.toString, order1.order_id.toString,matchTime.toString, matchQuantity.toString,matchPrice.toString))
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

  def readCSVFile(filepath: String): Unit = {

    val csvFile = new File(filepath)
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

      val order = Order(orderId, userName, orderTime, orderType, orderQty, orderPrice)

      orderBook = orderBook :+ order


    }

  }

  def writeToCSV(filepath : String, orders : Seq[String]) : Unit = {

    val writer = CSVWriter.open(filepath,true)
    writer.writeRow(orders)
    writer.close()
  }

}



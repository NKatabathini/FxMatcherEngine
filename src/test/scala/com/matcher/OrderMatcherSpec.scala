package com.matcher
import com.matcher.model.Order
import org.scalatest._

import java.io.File

class OrderMatcherSpec extends FlatSpec with Matchers {

  val expectedNumberOfOrders = 24
  "OrderMatcher" should "read the CSV file correctly" in {
    // Create a test CSV file with sample orders
    val testCSVFile = "src/test/resources/testOrders.csv"

    // Call the main method of OrderMatcher
    OrderMatcher.readCSVFile(testCSVFile)

    // Assert the expected number of orders in the orderBook
    OrderMatcher.orderBook.length should be(expectedNumberOfOrders)

    // Assert the correctness of individual order details if needed
    // OrderMatcher.orderBook.foreach { order =>
    //   // Perform assertions on order details
    // }
  }

  it should "process orders and execute trades correctly" in {
    // Create a mock orderBook with a few orders
    val mockOrderBook = List(
      Order(1, "user1", 123456789L, "BUY", 100, 10.0f),
      Order(2, "user2", 123456790L, "SELL", 100, 12.0f),
      Order(3, "user3", 123456791L, "BUY", 50, 9.0f),
      Order(4, "user4", 123456792L, "SELL", 50, 11.0f)
    )
    OrderMatcher.orderBook = mockOrderBook
  println("------------------------------------------------------------")
    // Create a new order to process
    val newOrder = Order(5, "user5", 123456793L, "SELL", 100, 9.5f)

    // Call the processOrder method for the new order
    OrderMatcher.processOrder(newOrder)

    // Assert the updated orderBook after processing the order
    val expectedOrderBook = List(
      Order(2, "user2", 123456790L, "SELL", 100, 12.0f),
      Order(3, "user3", 123456791L, "BUY", 50, 9.0f),
      Order(4, "user4", 123456792L, "SELL", 50, 11.0f)
    )
    OrderMatcher.orderBook should contain theSameElementsAs expectedOrderBook
  }

  it should "execute trades correctly" in {
    // Create mock Order objects representing a trade
    val order1 = Order(1, "user1", 123456789L, "BUY", 100, 10.0f)
    val order2 = Order(2, "user2", 123456790L, "SELL", 100, 12.0f)

    // Call the executeTrade method with the mock orders
    OrderMatcher.executeTrade(order1, order2)

    // Assert the expected output to the console
    val expectedOutput = s"Order ${order2.order_id} matched with ${order1.order_id} time ${System.currentTimeMillis()}, quantity ${order1.order_qty}, at price ${order1.order_price}"
//    val consoleOutput = // Capture the console output during test execution
     // consoleOutput should include(expectedOutput)
  }

  it should "find the best price order correctly" in {
    // Create mock orders with different prices and order types
    val BUYorders = List(
      Order(1, "user1", 123456789L, "BUY", 100, 10.0f),
      Order(3, "user3", 123456791L, "BUY", 50, 9.0f)
    )

  val SELLorders = List(
    Order(2, "user2", 123456790L, "SELL", 100, 12.0f),
    Order(4, "user4", 123456792L, "SELL", 50, 11.0f)
  )

    // Test finding the best price order for BUY type
    val bestBuyOrder = OrderMatcher.findBestPriceOrder("BUY", SELLorders)
    bestBuyOrder should be(Some(Order(4, "user4", 123456792L, "SELL", 50, 11.0f)))

    // Test finding the best price order for SELL type
    val bestSellOrder = OrderMatcher.findBestPriceOrder("SELL", BUYorders)
    bestSellOrder should be(Some(Order(1, "user1", 123456789L, "BUY", 100, 10.0f)))
  }

}

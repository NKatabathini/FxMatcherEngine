package com.matcher.model

case class Order(
                order_id : Int,
                username : String,
                order_time : Long,
                order_type : String,
                order_qty : Int,
                order_price : Float
                )

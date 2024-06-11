package bll.services

import bll.models.Order
import bll.models.OrderStatus

interface Service {
    fun createOrder(
        userId: Int,
        fromStationId: Int,
        toStationId: Int,
        status: OrderStatus
    ): Int

    fun getById(id: Int): Order?

    fun getChecked(): List<Order>

    fun update(id: Int, newValue: Order)
}
package dal.repositories

import dal.models.Order

interface Repository {
    fun create(userId: Int,
               fromStationId: Int,
               toStationId: Int,
               status: Int
    ): Int

    fun getById(id: Int): Order?
    fun getChecked(): List<Order>

    fun update(id: Int, newValue: Order)
}
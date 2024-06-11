package bll.services

import bll.models.Order
import bll.models.OrderStatus
import bll.models.toInt
import bll.models.toOrderStatus
import bll.exceptions.InvalidArgumentException
import dal.exceptions.StationNotFoundException
import org.springframework.stereotype.Service
import dal.repositories.Repository

import bll.models.Station as bllStation
import dal.models.Station as dalStation
import bll.models.Order as bllOrder
import dal.models.Order as dalOrder

@Service
class ServiceImplementation (private val repository: Repository): bll.services.Service {
    override fun createOrder(
        userId: Int,
        fromStationId: Int,
        toStationId: Int,
        status: OrderStatus): Int {

        if (fromStationId == toStationId) {
            throw InvalidArgumentException("Станция назначения не может совпадать со станцией отправления", "toStationId")
        }

        try {
            val statusInt = status.toInt()

            return repository.create(userId, fromStationId, toStationId, statusInt)
        }
        catch (ex: StationNotFoundException) {
            throw InvalidArgumentException(ex.message!!)
        }
    }

    override fun getById(id: Int): bllOrder? {
        val orderFromRepository: dalOrder? = repository.getById(id)

        return orderFromRepository?.let { order ->
            bllOrder(
                id = order.id,
                userId = order.userId,
                fromStation = bllStation(order.fromStation.id, order.fromStation.name),
                toStation = bllStation(order.toStation.id, order.toStation.name),
                status = order.status.toOrderStatus(),
                created = order.created
            )
        }

    }

    override fun getChecked(): List<bllOrder> {
        return repository.getChecked().map { order ->
            bllOrder(
                id = order.id,
                userId = order.userId,
                fromStation = bllStation(order.fromStation.id, order.fromStation.name),
                toStation = bllStation(order.toStation.id, order.toStation.name),
                status = order.status.toOrderStatus(),
                created = order.created
            )
        }
    }

    override fun update(id: Int, newValue: Order) {
        val orderToChange = getById(id) ?: throw IllegalArgumentException("No order with such id: $id")

        val fromStation = dalStation(newValue.fromStation.id, newValue.fromStation.name)
        val toStation = dalStation(newValue.toStation.id, newValue.toStation.name)

        repository.update(
            id,
            dalOrder(
                orderToChange.id,
                newValue.userId,
                fromStation,
                toStation,
                newValue.status.toInt(),
                orderToChange.created))
    }
}
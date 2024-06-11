package api.controllers

import api.requests.CreateOrderRequest
import api.requests.GetOrderRequest
import api.responses.CreateOrderResponse
import api.responses.GetOrderResponse
import bll.models.OrderStatus
import bll.models.toOrderStatus
import bll.exceptions.InvalidArgumentException
import bll.services.Service
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import kotlin.random.Random

@RestController
@RequestMapping("/api/v1/orders")
class Controller(private val service: Service) {
    @PostMapping("/create")
    fun createOrder(@RequestBody request: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
        val orderId =
        try {
            service.createOrder(
                request.userId,
                request.fromStationId,
                request.toStationId,
                request.status.toOrderStatus())
        } catch (ex: InvalidArgumentException) {
            return ResponseEntity(null, HttpStatusCode.valueOf(400))
        } catch (ex: Exception) {
            return ResponseEntity(null, HttpStatusCode.valueOf(500))
        }


        var order = service.getById(orderId)!!
        val thread = Thread {
            run {
                var newStatus = OrderStatus.Success
                if (Random.nextBoolean()) {
                    newStatus = OrderStatus.Rejection
                }
                Thread.sleep(1000)
                service.update(orderId, bll.models.Order(order.id,
                    order.userId,
                    order.fromStation,
                    order.toStation,
                    newStatus,
                    order.created))
            }
        }

        // Start the thread
        thread.start()

        return ResponseEntity(CreateOrderResponse(orderId), HttpStatusCode.valueOf(200))
    }

    @PostMapping("/get")
    fun getOrder(@RequestBody request: GetOrderRequest) : ResponseEntity<GetOrderResponse> {
        val order = service.getById(request.orderId) ?: return ResponseEntity(null, HttpStatusCode.valueOf(404))

        return ResponseEntity(GetOrderResponse(order), HttpStatusCode.valueOf(200))
    }
}
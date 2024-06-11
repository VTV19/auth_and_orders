package api.responses

class CreateOrderResponse(createdOrderId: Int) {
    val message: String = "Заказ создан. Id: $createdOrderId"
}
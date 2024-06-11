package api.requests

class CreateOrderRequest (
    val userId: Int,
    val fromStationId: Int,
    val toStationId: Int,
    val status: Int
) {}
package dal.models

import java.time.LocalDateTime

data class Order(
    val id: Int,
    val userId: Int,
    val fromStation: Station,
    val toStation: Station,
    var status: Int,
    val created: LocalDateTime
)
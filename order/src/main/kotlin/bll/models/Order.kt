package bll.models
import java.time.LocalDateTime

/**
 * remarks: В слоях bll и dal созданы классы, по сути являющиеся одним и тем же, чтобы сделать систему более гибкой:
 * впоследствии сущность на одном уровне можно будет изменить, не меняя логику другого уровня (с точнстью до замены
 * алгоритма маппинга)
 */
data class Order(
    val id: Int,
    val userId: Int,
    val fromStation: Station,
    val toStation: Station,
    var status: OrderStatus,
    val created: LocalDateTime
)
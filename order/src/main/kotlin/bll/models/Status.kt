package bll.models

import bll.exceptions.InvalidArgumentException

/**
 * remarks: В слоях bll и dal созданы классы, по сути являющиеся одним и тем же, чтобы сделать систему более гибкой:
 * впоследствии сущность на одном уровне можно будет изменить, не меняя логику другого уровня (с точнстью до замены
 * алгоритма маппинга)
 */
enum class OrderStatus {
    Check,
    Success,
    Rejection
}

fun OrderStatus.toInt(): Int {
    return when (this) {
        OrderStatus.Check -> 1
        OrderStatus.Success -> 2
        OrderStatus.Rejection -> 3

        else -> throw NotImplementedError()
    }
}

fun Int.toOrderStatus(): OrderStatus {
    return when(this) {
        1 -> OrderStatus.Check
        2 -> OrderStatus.Success
        3 -> OrderStatus.Rejection

        else -> throw InvalidArgumentException("Недопустимое значение статуса. Статус заказа должен быть в отрезке [1, 3]")
    }
}
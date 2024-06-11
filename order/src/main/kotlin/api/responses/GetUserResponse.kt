package api.responses

import java.util.*

/**
 * Экземпляр этого класса мы получаем от сервиса авторизации
 */
data class GetUserResponse(
    val id: Long,
    val nickname: String,
    val email: String,
    val created: Date
)
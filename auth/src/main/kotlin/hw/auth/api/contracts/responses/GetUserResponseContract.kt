package hw.auth.api.contracts.responses

import java.util.Date

data class GetUserResponseContract(
    val id: Long,
    val nickname: String,
    val email: String,
    val created: Date
)
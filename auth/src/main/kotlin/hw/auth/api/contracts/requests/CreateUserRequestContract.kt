package hw.auth.api.contracts.requests


data class CreateUserRequestContract(
    val nickname: String,
    val email: String,
    val password: String
)
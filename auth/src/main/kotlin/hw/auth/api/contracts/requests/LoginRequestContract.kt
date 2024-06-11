package hw.auth.api.contracts.requests

data class LoginRequestContract(
    val nickname: String,
    val password: String)
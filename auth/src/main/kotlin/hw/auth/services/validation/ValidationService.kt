package hw.auth.services.validation

interface ValidationService {
    fun throwOnValidationRulesViolation(
        nickname: String,
        email: String,
        password: String )

    fun throwIfUserExists(
        nickname: String,
        email: String)
}
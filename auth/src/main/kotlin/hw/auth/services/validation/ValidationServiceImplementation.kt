package hw.auth.services.validation

import hw.auth.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class ValidationServiceImplementation(private val userRepository: UserRepository): ValidationService {
    override fun throwOnValidationRulesViolation(nickname: String, email: String, password: String) {
        if (password.length < 8) {
            throw ValidationException("Пароль должен быть не меньше 8 символов!", "password")
        }

        nickname.throwIfEmpty("Имя пользователя не должно быть пустым!", "nickname")
        email.throwIfEmpty("Почта не должна быть пустой строкой!", "email")
        password.throwIfEmpty("Пароль не должен быть пустым!", "password")

        email.throwIfNotEmail()

        password.throwIfNotPassword()
    }

    override fun throwIfUserExists(nickname: String, email: String) {
        val userByNickname = userRepository.findByNickname(nickname)
        if (userByNickname != null) {
            throw ValidationException("Nickname: \'$nickname\' - уже занят", "nickname")
        }

        val userByEmail = userRepository.findByEmail(email)
        if (userByEmail != null) {
            throw ValidationException("Email: \'$email\' - уже занят", "email")
        }
    }
}

fun String.throwIfEmpty(message: String, argumentName: String) {
    if (this.isEmpty()) {
        throw ValidationException(message, argumentName)
    }
}

fun String.throwIfNotEmail(argumentName: String = "email"){
    val pattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}\$"

    if (!Regex(pattern).matches(this)) {
        throw ValidationException("Неправильный формат почты", argumentName)
    }
}

fun String.throwIfNotPassword(argumentName: String = "password") {
    val pattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]{8,}\$"

    if (!Regex(pattern).matches(this)) {
        throw ValidationException("Пароль должен состоять из не менее восьми символов," +
                " включая буквы обоих регистров, цифры и специальные символы;", argumentName)
    }
}
package hw.auth.services

import hw.auth.api.exceptions.UserNotFoundException
import hw.auth.models.User
import hw.auth.repositories.UserRepository
import hw.auth.models.Session
import hw.auth.repositories.SessionRepository
import hw.auth.services.validation.ValidationService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*


@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager,
    private val validationService: ValidationService,
    private val sessionRepository: SessionRepository
) {
    fun register(nickname: String, email: String, password: String): Long {
        validationService.throwOnValidationRulesViolation(nickname, email, password)
        validationService.throwIfUserExists(nickname, email)

        val user = User(
            nickname = nickname,
            email = email,
            password = passwordEncoder.encode(password)
        )
        userRepository.save(user)

        return user.id
    }

    fun login(nickname: String, password: String): String {
        val user = userRepository.findByNickname(nickname)
            ?: throw UserNotFoundException(nickname)

        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                nickname,
                password
            )
        )

        val token = jwtService.generateToken(user)

        try {
            sessionRepository.deleteByUserId(user.id)
        } catch (_: Exception) {}
        sessionRepository.save(createSession(user.id, token.token, token.expiration))

        return token.token
    }

    private fun createSession(userId: Long, token: String, expiration: Date): Session {
        return Session(
            userId = userId,
            token = token,
            expires = expiration
        )
    }
}
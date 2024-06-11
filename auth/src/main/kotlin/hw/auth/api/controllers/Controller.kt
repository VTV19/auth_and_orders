package hw.auth.api.controllers

import hw.auth.repositories.UserRepository
import hw.auth.api.contracts.requests.CreateUserRequestContract
import hw.auth.api.contracts.requests.LoginRequestContract
import hw.auth.api.contracts.responses.GetUserResponseContract
import hw.auth.api.contracts.responses.LoginResponseContract
import hw.auth.api.contracts.responses.RegisterResponseContract
import hw.auth.api.exceptions.UserNotFoundException
import hw.auth.services.AuthService
import hw.auth.services.validation.ValidationException
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class Controller(val authService: AuthService, val userRepository: UserRepository) {
    @PostMapping("/register")
    fun register(@RequestBody request: CreateUserRequestContract): ResponseEntity<RegisterResponseContract> {
        val id =
        try {
             authService.register(request.nickname, request.email, request.password)

        } catch (ex: ValidationException){
            return ResponseEntity(null, HttpStatusCode.valueOf(400))
        } catch (ex: Exception){
            return ResponseEntity(null, HttpStatusCode.valueOf(500))
        }
        return ResponseEntity.ok(RegisterResponseContract(id))
    }

    @PostMapping("/login")
    fun login(@RequestBody request: LoginRequestContract): ResponseEntity<LoginResponseContract> {
        val token = try {
            authService.login(request.nickname, request.password)
        } catch (ex: UserNotFoundException){
            return ResponseEntity(null, HttpStatusCode.valueOf(404))
        } catch (ex: BadCredentialsException) {
            return ResponseEntity(null, HttpStatusCode.valueOf(401))
        } catch (ex: Exception){
            return ResponseEntity(null, HttpStatusCode.valueOf(500))
        }

        return ResponseEntity.ok(LoginResponseContract(request.nickname, token))
    }

    @GetMapping("/get")
    fun get() : ResponseEntity<GetUserResponseContract> {
        val authentication = SecurityContextHolder.getContext().authentication
        val userSpring = authentication.principal as UserDetails
        val userCustom = userRepository.findByNickname(userSpring.username)

        return ResponseEntity.ok(GetUserResponseContract(userCustom!!.id, userCustom.nickname, userCustom.email, userCustom.created!!))
    }
}
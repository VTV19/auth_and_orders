package api.interceptors

import api.responses.GetUserResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.HandlerInterceptor

@Component
class Interceptor(private val rest: RestTemplate = RestTemplate()): HandlerInterceptor {
    private val logger = LoggerFactory.getLogger(Interceptor::class.java)

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any
    ): Boolean {
        logger.info("Запрос по адресу: ${request.requestURL}")
        try {
            val token = request.getHeader("Authorization")

            val user = getUserFromAuthService(token)
            if (user == null) {
                response.status = 401
                return false
            }

            return true
        } catch (ex: Exception) {
            response.status = 401
            return false
        }
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: org.springframework.web.servlet.ModelAndView?
    ) {}

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {}

    private fun getUserFromAuthService(token: String?): GetUserResponse? {
        val tokenBeginning = "Bearer "
        val url = "http://auth-api:8081/auth/get"

        if (token == null || !token.startsWith(tokenBeginning)) {
            throw Exception()
        }

        return try {
            val pureToken = token.substring(tokenBeginning.length)
            val request = HttpEntity<String>(HttpHeaders().apply {set("Authorization", "$tokenBeginning$pureToken")})

            return rest.exchange(url, HttpMethod.GET, request, GetUserResponse::class.java).body
        } catch (ex: Exception) {
            null
        }
    }
}

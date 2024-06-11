package hw.auth.api

import hw.auth.services.ApiUserService
import hw.auth.services.JwtService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class JwtFilter(
    private val jwtService: JwtService,
//    private val apiSessionRepository: ApiSessionRepository,
    private val apiUserService: ApiUserService
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response)
            return
        }

        val token = authHeader.substring(7)
        val claims = jwtService.extractAllClaims(token)

        if (claims != null && claims.subject != null && SecurityContextHolder.getContext().authentication == null) {
            val user = apiUserService.loadUserByUsername(claims.subject)

            if (jwtService.isValid(token, user)) {
                val authToken = UsernamePasswordAuthenticationToken(user, null, user.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            } else {
//                apiSessionRepository.deleteByToken(token)
            }
        }

        filterChain.doFilter(request, response)
    }
}
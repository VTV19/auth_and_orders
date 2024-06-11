package hw.auth.services


import hw.auth.models.User
import io.jsonwebtoken.Claims
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.*
import javax.crypto.SecretKey

@Service
class JwtService {
    private val secretKey = "my0320character0ultra0secure0and0ultra0long0secret"
    private val tokenTtl = 24 * 60 * 60 * 1000

    fun extractAllClaims(token: String): Claims? {
        return try {
            Jwts.parser().verifyWith(signingKey())
                .build().parseSignedClaims(token).payload
        } catch (e: ExpiredJwtException) {
            null
        }
    }

    fun isValid(token: String, user: UserDetails): Boolean {
        val claims = extractAllClaims(token)
        return claims != null
                && claims.subject == user.username
                && claims.expiration.after(Date())
    }

    data class Token(
        val token: String,
        val expiration: Date
    )

    fun generateToken(user: User): Token {
        val expiration = Date(System.currentTimeMillis() + tokenTtl)
        val token = Jwts.builder().subject(user.nickname)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expiration)
            .signWith(signingKey())
            .compact()

        return Token(token, expiration)
    }

    private fun signingKey(): SecretKey {
        val keyBytes = Decoders.BASE64.decode(secretKey)
        return Keys.hmacShaKeyFor(keyBytes)
    }
}
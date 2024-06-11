package hw.auth.repositories

import hw.auth.models.Session
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionRepository : JpaRepository<Session, Long> {
    fun findByUserId(userId: Long): Session?
    fun findByToken(token: String): Session?

    fun deleteByUserId(userId: Long): Session?
}
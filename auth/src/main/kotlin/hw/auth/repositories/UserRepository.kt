package hw.auth.repositories

import hw.auth.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findByNickname(nickname: String): User?
    fun findByEmail(email: String): User?
}
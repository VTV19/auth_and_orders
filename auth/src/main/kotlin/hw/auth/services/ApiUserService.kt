package hw.auth.services


import hw.auth.models.UserRole
import hw.auth.repositories.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class ApiUserService(
    private val userRepository: UserRepository,
    //private val apiSessionRepository: ApiSessionRepository,
) : UserDetailsService {
    override fun loadUserByUsername(nickname: String?): UserDetails {
        if (nickname == null) {
            throw UsernameNotFoundException("There is no such user")
        }

        val user = userRepository.findByNickname(nickname)
            ?: throw UsernameNotFoundException("There is no such user")

        return User(user.nickname, user.password, Collections.singleton(UserRole.USER))
    }

//    fun getUserInfo(nickname: String): UserInfoResponse {
//        val user = apiUserRepository.findByNickname(nickname)
//            ?: throw ApiException("There is no user")
//
//        val session = apiSessionRepository.findByUserId(user.id)
//            ?: throw ApiException("Invalid session, please login")
//
//        return UserInfoResponse(session)
//    }
}
package hw.auth.models

import org.springframework.security.core.GrantedAuthority

enum class UserRole : GrantedAuthority {
    USER;

    override fun getAuthority(): String {
        return name
    }
}
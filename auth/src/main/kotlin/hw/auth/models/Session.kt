package hw.auth.models

import jakarta.persistence.*
import java.util.Date

@Entity
@Table(name = "session")
data class Session(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false, length = 255)
    val token: String,

    @Column(nullable = false)
    val expires: Date
) {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        insertable = false,
        updatable = false
    )
    lateinit var user: User
}
package tano.testingpitfalls.domain.persistence

import jakarta.persistence.*


@Entity
class PersonEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    var name: String,

    @Enumerated(EnumType.STRING)
    var status: PersonStatus = PersonStatus.LEAD,

    ) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PersonEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}

enum class PersonStatus {
    LEAD,
    CLIENT,
}

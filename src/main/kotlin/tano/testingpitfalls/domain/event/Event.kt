package tano.testingpitfalls.domain.event

data class Event(
    val personId: Long,
    val type: String,
)

const val EVENT_TYPE_LOYALTY_PROGRAM_ENTERED = "LOYALTY_PROGRAM_ENTERED"
const val EVENT_TYPE_BONUS_POINTS_ADDED = "BONUS_POINTS_ADDED"

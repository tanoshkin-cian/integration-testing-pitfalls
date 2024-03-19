package tano.testingpitfalls.domain.web

data class Person(
    val id: Long?,
    val name: String?,
    val greetingMessageSent: Boolean = false,
    val bonusBalance: Long = 0,
    val status: PersonStatus = PersonStatus.LEAD,
)

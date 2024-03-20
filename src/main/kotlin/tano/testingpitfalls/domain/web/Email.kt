package tano.testingpitfalls.domain.web

data class AddEmailRequest(
    val email: String
)

data class EmailInfo(
    val email: String,
    val confirmationCode: String? = null,
    val isVerified: Boolean = false,
)

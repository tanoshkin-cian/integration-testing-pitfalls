package tano.testingpitfalls.controller

import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.persistence.EmailInformation
import tano.testingpitfalls.domain.web.EmailInfo

@Service
class EmailMapper {

    fun mapToWebFormat(email: EmailInformation): EmailInfo {
        return EmailInfo(
            email = email.email,
            confirmationCode = email.confirmationId,
            isVerified = email.isVerified
        )
    }

}

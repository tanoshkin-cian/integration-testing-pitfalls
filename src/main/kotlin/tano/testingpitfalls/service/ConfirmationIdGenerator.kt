package tano.testingpitfalls.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class ConfirmationIdGenerator {

    fun generateConfirmationId() = UUID.randomUUID().toString()

}

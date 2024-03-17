package tano.testingpitfalls.service.handler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.event.EVENT_TYPE_BONUS_POINTS_ADDED
import tano.testingpitfalls.domain.event.EVENT_TYPE_LOYALTY_PROGRAM_ENTERED
import tano.testingpitfalls.domain.event.Event
import tano.testingpitfalls.service.PersonService

@Service(EVENT_TYPE_LOYALTY_PROGRAM_ENTERED)
class LoyaltyProgramEnteredHandler(
    private val personService: PersonService
): EventHandler {

    private val logger = KotlinLogging.logger {}
    override fun handleEvent(event: Event): Event {
        logger.info { "Handling event $event" }
        val personId = event.personId
        personService.accrueWelcomeBonuses(personId = personId)
        logger.info { "Successfully handed event $event" }
        return Event(personId = personId, type = EVENT_TYPE_BONUS_POINTS_ADDED)
    }
}

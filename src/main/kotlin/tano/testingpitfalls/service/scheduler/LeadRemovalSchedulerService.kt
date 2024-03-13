package tano.testingpitfalls.service.scheduler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import tano.testingpitfalls.service.PersonService

@Service
class LeadRemovalSchedulerService(
    private val personService: PersonService,
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(fixedRate = 10)
    fun removeLeads() {
        val count = personService.removeLeads()
        if (count > 0) {
            logger.info { "Removed $count leads" }
        }
    }

}

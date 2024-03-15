package tano.testingpitfalls.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Clock

@Service
class GreetingService(
    private val clock: Clock
) {

    private val logger = KotlinLogging.logger {}

    fun greet(name: String): Boolean {
        if (clock.workingHours()) {
            // We will skip the sending logic itself for the sake of simplicity
            logger.info { "Greeting message for $name was sent successfully" }
            return true
        } else {
            logger.info { "Greeting message for $name was not sent because it's not working hours" }
            return false
        }
    }

}

fun Clock.workingHours(): Boolean {
    val hour = this.instant().atZone(this.zone).hour
    return hour in 9..18
}

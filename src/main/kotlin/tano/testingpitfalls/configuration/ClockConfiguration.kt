package tano.testingpitfalls.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

@Configuration
class ClockConfiguration {

    @Bean
    fun clock(): Clock {
        // here in order to reproduce the clock pitfall we create fixed clock with nighttime
        // however in real life we should use system default clock
        val nightTime = ZonedDateTime.now().withHour(2).withMinute(0).withSecond(0).withNano(0)
        return Clock.fixed(nightTime.toInstant(), ZoneId.systemDefault())
    }

}

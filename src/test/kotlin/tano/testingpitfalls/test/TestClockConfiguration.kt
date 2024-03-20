package tano.testingpitfalls.test

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.time.Clock
import java.time.ZoneId
import java.time.ZonedDateTime

@TestConfiguration
class TestClockConfiguration {

    @Bean
    @Primary
    fun workingHoursClock(): Clock {
        val workingTime = ZonedDateTime.now().withHour(10).withMinute(0).withSecond(0).withNano(0)
        return Clock.fixed(workingTime.toInstant(), ZoneId.systemDefault())
    }

}

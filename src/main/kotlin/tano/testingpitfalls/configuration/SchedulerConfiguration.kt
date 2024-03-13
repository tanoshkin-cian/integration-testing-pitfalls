package tano.testingpitfalls.configuration

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling

@Configuration
@ConditionalOnProperty(prefix = "scheduler", name = ["disabled"], havingValue = "false", matchIfMissing = true)
@EnableScheduling
class SchedulerConfiguration

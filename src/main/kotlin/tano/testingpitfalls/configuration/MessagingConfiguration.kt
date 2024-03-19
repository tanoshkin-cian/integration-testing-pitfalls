package tano.testingpitfalls.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import tano.testingpitfalls.domain.event.Event
import tano.testingpitfalls.service.handler.EventDispatcher
import java.util.function.Consumer

@Configuration
class MessagingConfiguration(
    private val eventDispatcher: EventDispatcher,
) {

    @Bean
    fun processEvents() = Consumer<Event> {
        eventDispatcher.processEvent(event = it)
    }

}

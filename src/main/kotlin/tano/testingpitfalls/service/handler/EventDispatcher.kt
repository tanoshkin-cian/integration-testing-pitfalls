package tano.testingpitfalls.service.handler

import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.event.Event

@Service
class EventDispatcher(
    private val handlers: Map<String, EventHandler>
) {

    fun processEvent(event: Event) {
        val type = event.type
        handlers[type]?.handleEvent(event)
    }

}

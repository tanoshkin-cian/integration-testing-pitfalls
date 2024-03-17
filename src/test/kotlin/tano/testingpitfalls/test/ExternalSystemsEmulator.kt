package tano.testingpitfalls.test

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.event.Event

@Service
class ExternalSystemsEmulator(
    private val streamBridge: StreamBridge,
) {

    fun sendEvent(eventType: String, userId: Long) {
        streamBridge.send("events-out-0", Event(personId = userId, type = eventType))
    }

}

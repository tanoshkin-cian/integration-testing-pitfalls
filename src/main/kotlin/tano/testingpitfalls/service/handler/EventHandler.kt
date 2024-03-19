package tano.testingpitfalls.service.handler

import tano.testingpitfalls.domain.event.Event

interface EventHandler {

    fun handleEvent(event: Event)

}

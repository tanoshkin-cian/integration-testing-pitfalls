package tano.testingpitfalls.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tano.testingpitfalls.domain.web.Person
import tano.testingpitfalls.service.PersonService
import tano.testingpitfalls.service.mapping.PersonMapper

@RestController
@RequestMapping("/persons")
class PersonsController(
    private val personService: PersonService,
    private val personMapper: PersonMapper,
) {

    @RequestMapping(
        produces = ["application/json"],
        method = [RequestMethod.POST],
        consumes = ["application/json"]
    )
    fun createPerson(@RequestBody person: Person): ResponseEntity<Person> {
        val validatedPerson = personMapper.validateAndMap(person = person)
        val savedPerson = personService.savePerson(person = validatedPerson)
        val resultingPerson = personMapper.mapToWebFormat(person = savedPerson)
        return ResponseEntity.ok(resultingPerson)
    }

    @GetMapping
    fun findAllPersons(): ResponseEntity<List<Person>> {
        val allPersons = personService.findAllPersons().map { personMapper.mapToWebFormat(it) }
        return ResponseEntity.ok(allPersons)
    }

    @DeleteMapping("/{personId}")
    fun removePerson(@PathVariable personId: Long) {
        personService.removePerson(personId = personId)
    }

}
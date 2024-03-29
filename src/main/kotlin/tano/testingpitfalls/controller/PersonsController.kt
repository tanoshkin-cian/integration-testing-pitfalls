package tano.testingpitfalls.controller

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import tano.testingpitfalls.domain.web.AddEmailRequest
import tano.testingpitfalls.domain.web.EmailInfo
import tano.testingpitfalls.domain.web.ModifyStatusRequest
import tano.testingpitfalls.domain.web.Person
import tano.testingpitfalls.service.PersonService
import tano.testingpitfalls.service.mapping.PersonMapper

@RestController
@RequestMapping("/persons")
class PersonsController(
    private val personService: PersonService,
    private val personMapper: PersonMapper,
    private val emailMapper: EmailMapper,
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

    @PostMapping("/{personId}/email")
    fun confirmEmail(@PathVariable personId: Long, @RequestBody addEmailRequest: AddEmailRequest): ResponseEntity<EmailInfo> {
        val email = personService.confirmEmail(email = addEmailRequest.email, personId = personId)
        val emailInfo = emailMapper.mapToWebFormat(email = email)
        return ResponseEntity.ok(emailInfo)
    }

    @GetMapping
    fun findAllPersons(): ResponseEntity<List<Person>> {
        val allPersons = personService.findAllPersons().map { personMapper.mapToWebFormat(it) }
        return ResponseEntity.ok(allPersons)
    }

    @PatchMapping("/{personId}")
    fun modifyStatus(@PathVariable personId: Long, @RequestBody modifyPersonRequest: ModifyStatusRequest): ResponseEntity<Person> {
        val newPersonStatus = personMapper.map(modifyPersonRequest.status)
        val modifiedPerson = personService.modifyPersonStatus(personId = personId, newPersonStatus = newPersonStatus)
        val responseBody = personMapper.mapToWebFormat(person = modifiedPerson)
        return ResponseEntity.ok(responseBody)
    }

    @GetMapping("/{personId}")
    fun getPerson(@PathVariable personId: Long): ResponseEntity<Person> {
        val person = personService.findPerson(personId = personId)
        val responseBody = personMapper.mapToWebFormat(person = person)
        return ResponseEntity.ok(responseBody)
    }

    @DeleteMapping("/{personId}")
    fun removePerson(@PathVariable personId: Long) {
        personService.removePerson(personId = personId)
    }

}

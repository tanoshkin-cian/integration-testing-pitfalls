package tano.testingpitfalls.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.IllegalInputDataException
import tano.testingpitfalls.domain.persistence.EmailInformation
import tano.testingpitfalls.domain.persistence.PersonEntity
import tano.testingpitfalls.domain.persistence.PersonStatus
import tano.testingpitfalls.repository.PersonEntityRepository

@Service
class PersonService(
    private val personEntityRepository: PersonEntityRepository,
    private val greetingService: GreetingService,
    private val confirmationIdGenerator: ConfirmationIdGenerator,
) {

    fun savePerson(person: PersonEntity): PersonEntity {
        return personEntityRepository.save(person)
    }

    fun findPerson(personId: Long): PersonEntity {
        return personEntityRepository.findByIdOrNull(id = personId) ?: throw IllegalInputDataException("No person with ID $personId found")
    }

    fun findAllPersons(): List<PersonEntity> {
        return personEntityRepository.findAll()
    }

    fun removePerson(personId: Long) {
        personEntityRepository.deleteById(personId)
    }

    @Transactional
    fun modifyPersonStatus(personId: Long, newPersonStatus: PersonStatus): PersonEntity {
        val personForModification = personEntityRepository.findByIdOrNull(id = personId) ?: throw IllegalInputDataException("No person with ID $personId found")
        personForModification.status = newPersonStatus
        if (newPersonStatus == PersonStatus.CLIENT) {
            val greetingSent = greetingService.greet(name = personForModification.name)
            personForModification.greetingSent = greetingSent
        }
        return personForModification
    }

    @Transactional
    fun accrueWelcomeBonuses(personId: Long): Long {
        val personForModification = personEntityRepository.findByIdOrNull(id = personId) ?: throw IllegalInputDataException("No person with ID $personId found")
        if (personForModification.status != PersonStatus.CLIENT) {
            throw IllegalInputDataException("Person with ID $personId is not a client")
        }
        personForModification.bonusPoints += 100
        return personForModification.bonusPoints

    }

    @Transactional
    fun removeLeads(): Long {
        return personEntityRepository.deleteByStatusEquals(status = PersonStatus.LEAD)
    }

    @Transactional
    fun confirmEmail(personId: Long, email: String): EmailInformation {
        val foundPerson = personEntityRepository.findByIdOrNull(id = personId) ?: throw IllegalInputDataException("No person with ID $personId found")
        return foundPerson.emails.getOrElse(email) {
            val confirmationId = confirmationIdGenerator.generateConfirmationId()
            val emailInformation = EmailInformation(
                email = email,
                confirmationId = confirmationId,
                isVerified = false
            )
            foundPerson.emails[email] = emailInformation
            // We will skip the confirmation logic for now
            return emailInformation
        }
    }

}

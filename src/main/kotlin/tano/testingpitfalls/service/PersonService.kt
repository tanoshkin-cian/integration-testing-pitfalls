package tano.testingpitfalls.service

import jakarta.transaction.Transactional
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.IllegalInputDataException
import tano.testingpitfalls.domain.persistence.PersonEntity
import tano.testingpitfalls.domain.persistence.PersonStatus
import tano.testingpitfalls.repository.PersonEntityRepository

@Service
class PersonService(
    private val personEntityRepository: PersonEntityRepository
) {

    fun savePerson(person: PersonEntity): PersonEntity {
        return personEntityRepository.save(person)
    }

    fun findAllPersons(): List<PersonEntity> {
        return personEntityRepository.findAll()
    }

    fun removePerson(personId: Long) {
        personEntityRepository.deleteById(personId)
    }

    @Transactional
    fun modifyPersonStatus(personId: Long, newPersonStatus: PersonStatus) {
        val foundPerson = personEntityRepository.findByIdOrNull(id = personId) ?: throw IllegalInputDataException("No person with ID $personId found")
        foundPerson.status = newPersonStatus
    }

    @Transactional
    fun removeLeads(): Long {
        return personEntityRepository.deleteByStatusEquals(status = PersonStatus.LEAD)
    }

}

package tano.testingpitfalls.service

import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.persistence.PersonEntity
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

}

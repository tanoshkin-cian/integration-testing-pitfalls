package tano.testingpitfalls.service.mapping

import org.springframework.stereotype.Service
import tano.testingpitfalls.domain.IllegalInputDataException
import tano.testingpitfalls.domain.persistence.PersonEntity
import tano.testingpitfalls.domain.web.Person
import tano.testingpitfalls.domain.web.PersonStatus

@Service
class PersonMapper {

    fun validateAndMap(person: Person): PersonEntity {
        val name = person.name ?: throw IllegalInputDataException("Person name can't be null" )
        return PersonEntity(name = name)
    }

    fun mapToWebFormat(person: PersonEntity): Person {
        return Person(id = person.id, name = person.name)
    }

    fun map(status: PersonStatus): tano.testingpitfalls.domain.persistence.PersonStatus {
        return when (status) {
            PersonStatus.LEAD ->  tano.testingpitfalls.domain.persistence.PersonStatus.LEAD
            PersonStatus.CLIENT ->  tano.testingpitfalls.domain.persistence.PersonStatus.LEAD
        }
    }

}

package tano.testingpitfalls.repository

import org.springframework.data.jpa.repository.JpaRepository
import tano.testingpitfalls.domain.persistence.PersonEntity
import tano.testingpitfalls.domain.persistence.PersonStatus

interface PersonEntityRepository: JpaRepository<PersonEntity, Long> {
    fun deleteByStatusEquals(status: PersonStatus): Long
}

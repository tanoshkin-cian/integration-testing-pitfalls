package tano.testingpitfalls.repository

import org.springframework.data.jpa.repository.JpaRepository
import tano.testingpitfalls.domain.persistence.PersonEntity

interface PersonEntityRepository: JpaRepository<PersonEntity, Long>

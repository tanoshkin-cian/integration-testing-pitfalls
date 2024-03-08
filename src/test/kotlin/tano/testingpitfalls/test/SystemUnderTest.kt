package tano.testingpitfalls.test

import org.springframework.stereotype.Service
import tano.testingpitfalls.repository.PersonEntityRepository

@Service
class SystemUnderTest(
    private val repository: PersonEntityRepository,
) {

    fun cleanDb() {
        repository.deleteAll()
    }

}

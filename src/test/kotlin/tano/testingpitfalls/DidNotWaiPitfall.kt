package tano.testingpitfalls

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.HttpStatusCode
import org.springframework.test.context.ActiveProfiles
import org.springframework.web.client.RestClient
import org.springframework.web.client.toEntity
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import tano.testingpitfalls.domain.event.EVENT_TYPE_LOYALTY_PROGRAM_ENTERED
import tano.testingpitfalls.domain.web.Person
import tano.testingpitfalls.test.ExternalSystemsEmulator
import tano.testingpitfalls.test.SystemUnderTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
//TODO: uncomment next line to fix the tests
class DidNotWaiPitfall {

    @LocalServerPort
    private var serverPort: Int = 0

    lateinit var restClient: RestClient

    @Autowired
    private lateinit var systemUnderTest: SystemUnderTest

    @Autowired
    private lateinit var externalSystemsEmulator: ExternalSystemsEmulator

    companion object {
        @Container
        @ServiceConnection
        var postgresContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13"))

        @Container
        @ServiceConnection
        var kafkaContainer = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"))
    }

    @BeforeEach
    fun setUp() {
        systemUnderTest.cleanDb()
        restClient = RestClient.create("http://localhost:$serverPort")
    }

    @Test
    fun waitingPitfall() {
        // given
        val creationResponse =
            restClient.post().uri("/persons").body(mapOf("name" to "John Doe")).retrieve().toEntity<Person>()
        assertThat(creationResponse.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        val createdId = creationResponse.body?.id ?: fail { "Created person ID could not be null" }

        val modificationResponse =
            restClient.patch().uri("/persons/{createdId}", createdId).body(mapOf("status" to "CLIENT")).retrieve()
                .toBodilessEntity()
        assertThat(modificationResponse.statusCode).isEqualTo(HttpStatusCode.valueOf(200))

        // when
        externalSystemsEmulator.sendEvent(eventType = EVENT_TYPE_LOYALTY_PROGRAM_ENTERED, userId = createdId)

        // then
        val resultingPerson = restClient.get().uri("/persons/{createdId}", createdId).retrieve().toEntity<Person>()
        assertThat(resultingPerson.body?.bonusBalance).isEqualTo(100)
    }
}

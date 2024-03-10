package tano.testingpitfalls.dirtydb

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import org.springframework.web.client.toEntity
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import tano.testingpitfalls.domain.web.Person
import tano.testingpitfalls.test.SystemUnderTest


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.MethodName::class) // in order to reproduce assertion problem in a stable way
class DirtyDbPitfall {

    @LocalServerPort
    private var serverPort: Int = 0

    lateinit var restClient: RestClient

    @Autowired
    private lateinit var systemUnderTest: SystemUnderTest

    companion object {
        @Container
        @ServiceConnection
        var postgresContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13"))
    }

    @BeforeEach
    fun setUp() {
//        TODO: uncomment next line to fix the tests
//        systemUnderTest.cleanDb()
        restClient = RestClient.create("http://localhost:$serverPort")
    }

    @Test
    @DisplayName("Creation API test")
    fun createApiTest() {
        // given
        val wantedEntitiesCount = 4

        // when
        val creationResponses = (1..4).map {
            restClient.post().uri("/persons").body(mapOf("name" to "John Doe")).retrieve().toBodilessEntity()
        }
        assertThat(creationResponses).allMatch { it.statusCode == HttpStatusCode.valueOf(200) }

        // then
        val businessEntities = restClient.get().uri("/persons").retrieve().body<List<Person>>()
        assertThat(businessEntities?.size).isEqualTo(wantedEntitiesCount)
    }

    @Test
    @DisplayName("Removal API test")
    fun removeApiTest() {
        // given
        val creationResponse =
            restClient.post().uri("/persons").body(mapOf("name" to "John Doe")).retrieve().toEntity<Person>()
        assertThat(creationResponse.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        val createdId = creationResponse.body?.id ?: fail { "Created person ID could not be null" }

        // when
        val removalResponse =
            restClient.delete().uri("/persons/{createdId}", createdId).retrieve().toBodilessEntity()
        assertThat(removalResponse.statusCode).isEqualTo(HttpStatusCode.valueOf(200))

        // then
        val businessEntities = restClient.get().uri("/persons").retrieve().body<List<Person>>()
        assertThat(businessEntities?.size).isEqualTo(0)
    }


}

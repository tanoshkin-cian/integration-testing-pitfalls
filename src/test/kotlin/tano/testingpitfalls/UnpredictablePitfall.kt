package tano.testingpitfalls

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.assertj.core.api.Assertions
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
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import tano.testingpitfalls.domain.web.EmailInfo
import tano.testingpitfalls.domain.web.Person
import tano.testingpitfalls.service.ConfirmationIdGenerator
import tano.testingpitfalls.test.SystemUnderTest

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
class UnpredictablePitfall {

    @LocalServerPort
    private var serverPort: Int = 0

    lateinit var restClient: RestClient

    @Autowired
    private lateinit var systemUnderTest: SystemUnderTest

    @MockkBean
    private lateinit var confirmationIdGenerator: ConfirmationIdGenerator

    companion object {
        @Container
        @ServiceConnection
        var postgresContainer = PostgreSQLContainer(DockerImageName.parse("postgres:13"))
    }

    @BeforeEach
    fun setUp() {
        systemUnderTest.cleanDb()
        restClient = RestClient.create("http://localhost:$serverPort")
    }

    @Test
    fun testUnpredictable() {
        // given
        val creationResponse =
            restClient.post().uri("/persons").body(mapOf("name" to "John Doe")).retrieve().toEntity<Person>()
        Assertions.assertThat(creationResponse.statusCode).isEqualTo(HttpStatusCode.valueOf(200))
        val createdId = creationResponse.body?.id ?: fail { "Created person ID could not be null" }
        val email = "john@domain.com"
        val wantedConfirmationId = "CONFIRMATION_ID"
        //        TODO: uncomment next line to fix the tests
//        every { confirmationIdGenerator.generateConfirmationId() } returns wantedConfirmationId

        // when
        val emailResponse =
            restClient.post().uri("/persons/{createdId}/email", createdId).body(mapOf("email" to email)).retrieve()
                .toEntity<EmailInfo>()


        // then
        Assertions.assertThat(emailResponse.body?.isVerified).isEqualTo(false)
        Assertions.assertThat(emailResponse.body?.email).isEqualTo(email)
        Assertions.assertThat(emailResponse.body?.confirmationCode).isEqualTo(wantedConfirmationId)
    }
}

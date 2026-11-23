package hireline

import hireline.platform.Config
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.testcontainers.postgresql.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.Duration

class HealthEndToEndTest {
    private val image = DockerImageName.parse(System.getProperty("pgvectorImage"))
        .asCompatibleSubstituteFor("postgres")

    @Test
    fun `health is 200 while Postgres is up and 503 after it stops`() {
        val postgres = PostgreSQLContainer(image)
        postgres.start()
        val app = start(Config(postgres.jdbcUrl, postgres.username, postgres.password, httpPort = 0))
        try {
            assertEquals(200, healthStatus(app.port()))

            postgres.stop()

            assertEquals(503, healthStatus(app.port()))
        } finally {
            app.stop()
            postgres.stop()
        }
    }

    private fun healthStatus(port: Int): Int {
        val request = HttpRequest.newBuilder(URI("http://localhost:$port/health"))
            .timeout(Duration.ofSeconds(10))
            .build()
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.discarding()).statusCode()
    }
}

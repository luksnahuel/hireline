package hireline.platform

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.sql.SQLException
import kotlin.time.Duration.Companion.milliseconds

class RetryTest {
    @Test
    fun `succeeds after two connection failures`() {
        var attempts = 0

        retryConnection(timeoutMillis = 1000) {
            attempts++
            if (attempts <= 2) throw SQLException("refused", "08001")
        }

        assertEquals(3, attempts)
    }

    @Test
    fun `fails on the first attempt with a non-retryable SQLState`() {
        var attempts = 0

        val e = assertThrows<SQLException> {
            retryConnection(timeoutMillis = 1000) {
                attempts++
                throw SQLException("bad password", "28P01")
            }
        }

        assertEquals("28P01", e.sqlState)
        assertEquals(1, attempts)
    }

    @Test
    fun `gives up once the time budget runs out`() {
        var attempts = 0

        val e = assertThrows<SQLException> {
            retryConnection(timeoutMillis = 50) {
                attempts++
                throw SQLException("refused", "08001")
            }
        }

        assertEquals("08001", e.sqlState)
        assertTrue(attempts > 1, "attempts = $attempts")
    }

    private fun retryConnection(timeoutMillis: Int, action: () -> Unit) =
        retry(delay = 10.milliseconds, timeout = timeoutMillis.milliseconds, action = action)
}

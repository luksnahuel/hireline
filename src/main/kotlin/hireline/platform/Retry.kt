package hireline.platform

import org.slf4j.LoggerFactory
import java.sql.SQLException
import java.util.Locale
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

private val log = LoggerFactory.getLogger("hireline.platform.Retry")

private const val CONNECTION_FAILURE = "08001"

fun retry(
    delay: Duration = 1.seconds,
    timeout: Duration = 60.seconds,
    action: () -> Unit,
) {
    val start = TimeSource.Monotonic.markNow()
    var attempts = 0
    while (true) {
        attempts++
        try {
            action()
            return
        } catch (e: SQLException) {
            if (e.sqlState != CONNECTION_FAILURE) {
                log.error("not retrying: SQLState {}", e.sqlState)
                throw e
            }
            val elapsed = start.elapsedNow()
            if (elapsed >= timeout) {
                val seconds = "%.1f".format(Locale.ROOT, elapsed.toDouble(DurationUnit.SECONDS))
                log.error("gave up after {} attempts in {} s, last SQLState {}", attempts, seconds, e.sqlState)
                throw e
            }
            log.warn("attempt {} failed (SQLState {}), retrying in {}", attempts, e.sqlState, delay)
            Thread.sleep(delay.inWholeMilliseconds)
        }
    }
}

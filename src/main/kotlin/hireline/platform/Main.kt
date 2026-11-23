package hireline.platform

import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

private val log = LoggerFactory.getLogger("hireline.platform.Main")

fun main() {
    try {
        start(Config.fromEnv())
    } catch (e: Exception) {
        log.error("Startup failed", e)
        exitProcess(1)
    }
}

/** The startup sequence from docs/design/m0-skeleton.md. */
fun start(config: Config) {
    log.info("Starting with {}", config)
    val dataSource = createDataSource(config)
    waitForDatabase(dataSource)
    runMigrations(dataSource)
}

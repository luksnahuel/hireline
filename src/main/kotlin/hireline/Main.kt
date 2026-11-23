package hireline

import hireline.platform.Config
import hireline.platform.createDataSource
import hireline.platform.runMigrations
import hireline.platform.waitForDatabase
import hireline.webapi.createWebApp
import io.javalin.Javalin
import org.slf4j.LoggerFactory
import kotlin.system.exitProcess

private val log = LoggerFactory.getLogger("hireline.Main")

fun main() {
    try {
        start(Config.fromEnv())
    } catch (e: Exception) {
        log.error("Startup failed", e)
        exitProcess(1)
    }
}

/**
 * The startup sequence from docs/design/m0-skeleton.md. The HTTP server starts
 * only after migrations succeed. Returns the running server so callers can stop it.
 */
fun start(config: Config): Javalin {
    log.info("Starting with {}", config)
    val dataSource = createDataSource(config)
    waitForDatabase(dataSource)
    runMigrations(dataSource)
    return createWebApp(dataSource).start(config.httpPort)
}

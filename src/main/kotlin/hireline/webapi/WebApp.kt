package hireline.webapi

import io.javalin.Javalin
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import org.slf4j.LoggerFactory
import java.sql.SQLException
import javax.sql.DataSource

private val log = LoggerFactory.getLogger("hireline.webapi.Health")

/** The HTTP API. Not started here: the caller decides the port. */
fun createWebApp(dataSource: DataSource): Javalin =
    Javalin.create { config ->
        config.routes.get("/health") { ctx -> health(ctx, dataSource) }
    }

/**
 * 200 if Postgres answers SELECT 1. 503 if it can't be reached: that is a temporary
 * condition, not a bug. Any other exception is left to Javalin, which answers 500.
 */
private fun health(ctx: Context, dataSource: DataSource) {
    try {
        dataSource.connection.use { connection ->
            connection.createStatement().use { statement ->
                statement.queryTimeout = 3 // seconds
                statement.execute("SELECT 1")
            }
        }
        ctx.status(HttpStatus.OK).result("ok")
    } catch (e: SQLException) {
        log.warn("Health check failed: {}", e.message)
        ctx.status(HttpStatus.SERVICE_UNAVAILABLE).result("database unavailable")
    }
}

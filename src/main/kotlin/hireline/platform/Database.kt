package hireline.platform

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

/**
 * A plain, unpooled DataSource: every getConnection() opens a new connection.
 * Enough for M0 (startup and /health); a pool can replace it when features need one.
 */
fun createDataSource(config: Config): DataSource =
    PGSimpleDataSource().apply {
        setURL(config.dbUrl)
        user = config.dbUser
        password = config.dbPassword
        connectTimeout = 3 // seconds; fail fast when Postgres is unreachable
    }

/**
 * Startup step 2: make sure Postgres accepts connections.
 *
 * TODO(you): retry loop. Bounded number of attempts with a delay between them;
 *  after the last failed attempt, throw. main() turns any startup exception into
 *  "log the error and exit with status 1". Postgres took ~2-3s to start locally.
 *  For now this is a single attempt with no retry.
 */
fun waitForDatabase(dataSource: DataSource) {
    dataSource.connection.use { }
}

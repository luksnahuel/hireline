package hireline.platform

import org.flywaydb.core.Flyway
import javax.sql.DataSource

/**
 * Startup step 3: apply pending migrations from src/main/resources/db/migration.
 * Throws on any failure; it is not retried, because retrying won't fix a bad migration.
 */
fun runMigrations(dataSource: DataSource) {
    Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .load()
        .migrate()
}

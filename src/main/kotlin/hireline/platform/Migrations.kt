package hireline.platform

import org.flywaydb.core.Flyway
import javax.sql.DataSource

fun runMigrations(dataSource: DataSource) {
    Flyway.configure()
        .dataSource(dataSource)
        .locations("classpath:db/migration")
        .load()
        .migrate()
}

package hireline.platform

import org.postgresql.ds.PGSimpleDataSource
import javax.sql.DataSource

fun createDataSource(config: Config): DataSource =
    PGSimpleDataSource().apply {
        setURL(config.dbUrl)
        user = config.dbUser
        password = config.dbPassword
        connectTimeout = 3 // seconds
    }

fun waitForDatabase(dataSource: DataSource) {
    retryDatabaseConnection { dataSource.connection.use { } }
}

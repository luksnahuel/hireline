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

// TODO: retry with a bounded number of attempts, then throw (main exits with status 1).
fun waitForDatabase(dataSource: DataSource) {
    dataSource.connection.use { }
}

package hireline.platform

class Config(
    val dbUrl: String,
    val dbUser: String,
    val dbPassword: String,
    val httpPort: Int,
) {
    // Leaves out the password, so the config can be logged.
    override fun toString() = "Config(dbUrl=$dbUrl, dbUser=$dbUser, httpPort=$httpPort)"

    companion object {
        fun fromEnv(env: Map<String, String> = System.getenv()): Config {
            val names = listOf("DB_URL", "DB_USER", "DB_PASSWORD", "HTTP_PORT")
            val missing = names.filter { env[it].isNullOrBlank() }
            require(missing.isEmpty()) { "Missing environment variables: ${missing.joinToString()}" }

            val port = env.getValue("HTTP_PORT")
            return Config(
                dbUrl = env.getValue("DB_URL"),
                dbUser = env.getValue("DB_USER"),
                dbPassword = env.getValue("DB_PASSWORD"),
                httpPort = requireNotNull(port.toIntOrNull()) { "HTTP_PORT is not a number: $port" },
            )
        }
    }
}

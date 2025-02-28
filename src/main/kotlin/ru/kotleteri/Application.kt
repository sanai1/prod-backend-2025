package ru.kotleteri

import ru.kotleteri.database.DatabaseFactory
import ru.kotleteri.database.DatabaseInit
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import ru.kotleteri.plugins.configureHTTP
import ru.kotleteri.plugins.configureRouting
import ru.kotleteri.plugins.configureSecurity
import ru.kotleteri.plugins.configureSerialization
import ru.kotleteri.utils.POSTGRES_PASSWORD
import ru.kotleteri.utils.POSTGRES_URL
import ru.kotleteri.utils.POSTGRES_USER
import ru.kotleteri.utils.SERVER_PORT

fun main() {
    embeddedServer(Netty, port = SERVER_PORT, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}
fun Application.module() {
    while (true) {
        try {
            connect()
            break
        } catch (exception: Exception) {
            println("Нет подключения в БД")
        }
    }
    DatabaseInit.initialize()
    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureRouting()
}

fun connect() {
    Database.connect(
        DatabaseFactory.createHikariDataSource(
            POSTGRES_URL,
            "org.postgresql.Driver",
            POSTGRES_USER,
            POSTGRES_PASSWORD
        ),
    )
}


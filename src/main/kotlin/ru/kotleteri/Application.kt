package ru.kotleteri

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.jetbrains.exposed.sql.Database
import ru.kotleteri.database.DatabaseFactory
import ru.kotleteri.database.DatabaseInit
import ru.kotleteri.database.redis.RedisClientImpl
import ru.kotleteri.database.redis.RedisInit
import ru.kotleteri.plugins.*
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
    RedisInit.initialize(RedisClientImpl())
    configureDocumentation()
    configureSerialization()
    configureHTTP()
    configureSecurity()
    configureRouting()
    configureStatusPages()
    configureValidation()
    configureRateLimiting()
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


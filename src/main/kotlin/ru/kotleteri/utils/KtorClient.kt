package ru.kotleteri.utils

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import ru.kotleteri.plugins.JsonFormat

val client = HttpClient(CIO) {
    install(ContentNegotiation) {
        json(JsonFormat)
    }
}
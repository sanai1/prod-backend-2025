package ru.kotleteri.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
val JsonFormat = Json {
    explicitNulls = false
    encodeDefaults = true //кодировка дефолтных значений из дата классов
    isLenient = false
    allowSpecialFloatingPointValues = false
    allowStructuredMapKeys = true
    prettyPrint = false
    useArrayPolymorphism = false
    ignoreUnknownKeys = true
//    namingStrategy = JsonNamingStrategy.SnakeCase
}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(JsonFormat)
    }
}

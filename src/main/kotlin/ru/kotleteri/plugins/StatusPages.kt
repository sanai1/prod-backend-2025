package ru.kotleteri.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.CallAbortException
import ru.kotleteri.data.models.inout.ErrorResponse

fun Application.configureStatusPages() {
    install(StatusPages) {
        exception(CallAbortException::class) { call, exception ->
            call.respond(exception.code, ErrorResponse(exception.message ?: "Unknown error"))
        }
        exception<RequestValidationException> { call, exception ->
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(exception.reasons.joinToString()))
        }
    }
}
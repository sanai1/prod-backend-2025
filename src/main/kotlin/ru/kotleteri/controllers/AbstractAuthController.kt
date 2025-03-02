package ru.kotleteri.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

abstract class AbstractAuthController(val call: ApplicationCall) {
    val principal = call.principal<JWTPrincipal>()
    val email = principal!!.payload.getClaim("email").asString()
    val id = UUID.fromString(principal!!.payload.getClaim("id").asString())
    val isClient = principal!!.payload.getClaim("isClient").asBoolean()
}

class CallAbortException(val code: HttpStatusCode, message: String) : Exception("$code: $message")

fun abort(code: HttpStatusCode, message: String): Nothing {
    throw CallAbortException(code, message)
}
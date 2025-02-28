package ru.kotleteri.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.UUID

abstract class AbstractAuthController(val call: ApplicationCall) {
    val principal = call.principal<JWTPrincipal>()
    val email = principal!!.payload.getClaim("email").asString()
    val id = UUID.fromString(principal!!.payload.getClaim("id").asString())
    val isClient = principal!!.payload.getClaim("isClient").asBoolean()
}
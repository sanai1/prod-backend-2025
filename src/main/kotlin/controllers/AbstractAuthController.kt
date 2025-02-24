package ru.kotleteri.controllers

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

abstract class AbstractAuthController(val call: ApplicationCall) {
    val principal = call.principal<JWTPrincipal>()
    val email = principal!!.payload.getClaim("email").asString()
    val id = principal!!.payload.getClaim("id").asInt()
}
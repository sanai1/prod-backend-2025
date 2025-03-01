package ru.kotleteri.controllers.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse

class AuthCompanyController(call: ApplicationCall): AbstractAuthController(call) {
    suspend fun setPicture(){
        if (isClient){
            call.respond(HttpStatusCode.OK, ErrorResponse("You are not company"))
            return
        }

        val imageStream = call.receiveStream()

        try {

        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Something went wrong"))
            return
        }
    }
}
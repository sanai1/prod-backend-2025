package ru.kotleteri.controllers.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.ClientCRUD

class AuthClientController(call: ApplicationCall): AbstractAuthController(call) {
    suspend fun getProfile() {
        if (!isClient) {
            call.respond(HttpStatusCode.OK, ErrorResponse("You are not company"))
            return
        }

        val client = ClientCRUD.read(id)

        if (client == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Company not found"))
            return
        }

        call.respond(HttpStatusCode.OK, client.getProfile())
    }
}
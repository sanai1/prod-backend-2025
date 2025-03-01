package ru.kotleteri.controllers.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.enums.Gender
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.clients.AddClientTargetingDataModel
import ru.kotleteri.database.crud.ClientCRUD

class AuthClientController(call: ApplicationCall): AbstractAuthController(call) {
    suspend fun getProfile() {
        if (!isClient) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not company"))
            return
        }

        val client = ClientCRUD.read(id)

        if (client == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Company not found"))
            return
        }  // todo add target settings to GetClientProfileResponseModel

        call.respond(HttpStatusCode.OK, client.getProfile())
    }

    suspend fun editTarget() {
        if (!isClient) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not company"))
            return
        }

        val r = call.receive<AddClientTargetingDataModel>()

        val gender = when (r.gender.lowercase()) {
            "male" -> Gender.MALE
            "female" -> Gender.FEMALE
            else -> return call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid gender"))
        }

        ClientCRUD.addExtension(id, r.age, gender)

        call.respond(HttpStatusCode.OK)
    }
}
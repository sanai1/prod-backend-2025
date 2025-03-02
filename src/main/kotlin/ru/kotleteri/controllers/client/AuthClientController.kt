package ru.kotleteri.controllers.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.enums.Gender
import ru.kotleteri.data.models.base.ClientExtensionModel
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.clients.AddClientTargetingDataModel
import ru.kotleteri.database.crud.ClientCRUD

class AuthClientController(call: ApplicationCall) : AbstractAuthController(call) {

    init {
        if (!isClient) {
            abort(HttpStatusCode.Forbidden, "You are not client")
        }
    }

    suspend fun getProfile() {


        val client =
            ClientCRUD.read(id) ?: return call.respond(HttpStatusCode.NotFound, ErrorResponse("Client not found"))

        // todo add target settings to GetClientProfileResponseModel

        call.respond(HttpStatusCode.OK, client.getProfile())
    }

    suspend fun editTarget() {

        val r = call.receive<AddClientTargetingDataModel>()

        val gender = when (r.gender.lowercase()) {
            "male" -> Gender.MALE
            "female" -> Gender.FEMALE
            else -> return call.respond(HttpStatusCode.BadRequest, ErrorResponse("Invalid gender"))
        }

        ClientCRUD.addExtension(
            ClientExtensionModel(
                id,
                r.age,
                gender
            )
        )

        call.respond(HttpStatusCode.OK)
    }
}
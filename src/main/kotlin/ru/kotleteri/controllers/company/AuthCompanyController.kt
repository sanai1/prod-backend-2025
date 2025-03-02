package ru.kotleteri.controllers.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.integrations.objectstorage.ImageLoading

class AuthCompanyController(call: ApplicationCall) : AbstractAuthController(call) {

    init {
        if (isClient) {
            abort(HttpStatusCode.Forbidden, "You are not company")
        }
    }

    suspend fun getProfile() {

        val company =
            CompanyCRUD.read(id) ?: return call.respond(HttpStatusCode.NotFound, ErrorResponse("Company not found"))


        call.respond(HttpStatusCode.OK, company.getProfile())
    }

    suspend fun setPicture() {

        try {
            val imageBytes = call.receiveStream().readBytes()
            println("[BYTES]: $imageBytes")
            ImageLoading.saveImageToS3(id.toString(), imageBytes)

            call.respond(HttpStatusCode.OK)

        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Something went wrong"))
            return
        }
    }


}
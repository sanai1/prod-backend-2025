package ru.kotleteri.controllers.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.integrations.objectstorage.ImageLoading
import java.util.*

class AuthCompanyController(call: ApplicationCall) : AbstractAuthController(call) {
    suspend fun getProfile(){
        if (isClient) {
            call.respond(HttpStatusCode.OK, ErrorResponse("You are not company"))
            return
        }

        val company = CompanyCRUD.read(id)

        if (company == null) {
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Company not found"))
            return
        }

        call.respond(HttpStatusCode.OK, company.getProfile())
    }

    suspend fun setPicture() {
        if (isClient) {
            call.respond(HttpStatusCode.OK, ErrorResponse("You are not company"))
            return
        }

        try {
            val imageBytes = call.receiveStream().readBytes()
            ImageLoading.saveImageToS3(id.toString(), imageBytes)

            call.respond(HttpStatusCode.OK)

        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Something went wrong"))
            return
        }
    }

    suspend fun getPicture() {

        val companyId = try {
            UUID.fromString(call.parameters["companyId"]).toString()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("bad companyId"))
            return
        }

        try {
            val stream = ImageLoading.getImageFromS3(companyId)
            if (stream == null) {
                call.respond(HttpStatusCode.NotFound, ErrorResponse("Picture not found"))
                return
            }

            call.respondBytes(
                stream.readBytes(),
                ContentType.Image.JPEG,
                HttpStatusCode.OK
            )

        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Something went wrong"))
            return
        }
    }
}
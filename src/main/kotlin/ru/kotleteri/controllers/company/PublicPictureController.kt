package ru.kotleteri.controllers.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.integrations.objectstorage.ImageLoading
import java.util.*

class PublicPictureController(call: ApplicationCall) : AbstractAuthController(call) {
    suspend fun getPicture() {

        val companyId = try {
            UUID.fromString(call.parameters["companyId"]).toString()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("bad companyId"))
            return
        }

        try {
            val stream = ImageLoading.getImageFromS3(companyId) ?: return call.respond(
                HttpStatusCode.NotFound,
                ErrorResponse("Picture not found")
            )

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
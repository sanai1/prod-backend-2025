package ru.kotleteri.controllers.company

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.integrations.objectstorage.ImageLoading
import ru.kotleteri.utils.toUUIDOrNull

class PublicPictureController(val call: ApplicationCall) {
    suspend fun getPicture() {

        val companyId =
            call.parameters["companyId"]?.toUUIDOrNull()?.toString() ?: return call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("bad companyId")
            )

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
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message.toString()))
            return
        }
    }
}
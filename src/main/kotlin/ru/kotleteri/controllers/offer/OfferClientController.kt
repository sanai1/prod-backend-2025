package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.offers.GenerateQRPayloadResponseModel
import ru.kotleteri.data.models.redis.QRDataModel
import ru.kotleteri.database.crud.OfferCRUD
import ru.kotleteri.database.redis.QRService
import ru.kotleteri.utils.toUUIDOrNull
import java.time.LocalDateTime

class OfferClientController(call: ApplicationCall) : AbstractAuthController(call) {

    init {
        if (!isClient) {
            abort(HttpStatusCode.Forbidden, "You are not client")
        }
    }

    suspend fun getOffersList() {


        val limit = call.parameters["limit"]?.toIntOrNull() ?: return call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse("Wrong limit")
        )


        val offset =
            call.parameters["offset"]?.toLongOrNull() ?: return call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Wrong offset")
            )


        val offerList = OfferCRUD.readAll(limit, offset)
            .filter { it.second.startDate <= LocalDateTime.now() && it.second.endDate >= LocalDateTime.now() }
            .map { it.second.toGetOfferWithCompanyResponse(it.first) }


        call.respond(HttpStatusCode.OK, offerList)
    }

    suspend fun generateQrPayload() {
        val offerId =
            call.parameters["offerId"]?.toUUIDOrNull() ?: return call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Wrong offer id provided")
            )

        val spendBonus = when(call.parameters["spendBonus"]){
            "true" -> true
            else -> false
        }

        val offer =
            OfferCRUD.read(offerId) ?: return call.respond(HttpStatusCode.NotFound, ErrorResponse("Offer not found"))


        val qrPayload = QRService.generateCode(
            QRDataModel(
                id.toString(),
                offerId.toString(),
                spendBonus
            )
        )

        call.respond(HttpStatusCode.OK, GenerateQRPayloadResponseModel(qrPayload.toString()))
    }
}
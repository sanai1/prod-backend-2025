package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.offers.GenerateQRPayloadResponseModel
import ru.kotleteri.data.models.redis.QRDataModel
import ru.kotleteri.database.crud.OfferCRUD
import ru.kotleteri.database.redis.QRService
import java.util.UUID

class OfferClientController(call: ApplicationCall) : AbstractAuthController(call) {
    suspend fun getOffersList(){
        if (!isClient){
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not client"))
            return
        }

        val limit = try {
            call.parameters["limit"]!!.toInt()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Wrong limit"))
            return
        }

        val offset = try {
            call.parameters["offset"]!!.toLong()
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Wrong offset"))
            return
        }

        val offerList = OfferCRUD.readAll(limit, offset).map{it.second.toGetOfferWithCompanyResponse(it.first)}

        call.respond(HttpStatusCode.OK, offerList)
    }

    suspend fun generateQrPayload(){
        if (!isClient){
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not client"))
            return
        }

        val offerId = try {
            UUID.fromString(call.parameters["offerId"]!!)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("Wrong offer id provided"))
            return
        }

        val offer = OfferCRUD.read(offerId)

        if (offer == null){
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Offer not found"))
            return
        }

        val qrPayload = QRService.generateCode(
            QRDataModel(
                id.toString(),
                offerId.toString(),
            )
        )

        call.respond(HttpStatusCode.OK, GenerateQRPayloadResponseModel(qrPayload.toString()))
    }
}
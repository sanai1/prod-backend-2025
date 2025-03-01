package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.offers.GetOfferByQrRequestModel
import ru.kotleteri.data.models.inout.offers.GetOfferByQrResponseModel
import ru.kotleteri.database.crud.ClientCRUD
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.database.crud.OfferCRUD
import ru.kotleteri.database.redis.QRService
import java.util.*

class OfferCompanyController(call: ApplicationCall) : AbstractAuthController(call) {
    suspend fun getAllOffersByCompany(){
        if (isClient){
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not company"))
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

        val company = CompanyCRUD.read(id)!!

        val offersList = OfferCRUD.readByCompanyId(id, limit, offset).map {
            it.toGetOfferWithCompanyResponse(company.name)
        }

        call.respond(HttpStatusCode.OK, offersList)

    }

    suspend fun receiveOfferQr(){
        if (isClient){
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not company"))
            return
        }

        val r = call.receive<GetOfferByQrRequestModel>()

        val data = QRService.getCode(r.payload)

        if(data == null){
            call.respond(HttpStatusCode.NotFound, ErrorResponse("Qr is not found or expired"))
            return
        }

        val offer = OfferCRUD.read(UUID.fromString(data.offerId)) ?:
        return call.respond(HttpStatusCode.BadRequest, ErrorResponse("offer is null"))
        val client = ClientCRUD.read(UUID.fromString(data.clientId)) ?:
        return call.respond(HttpStatusCode.BadRequest, ErrorResponse("offer is null"))


        call.respond(HttpStatusCode.OK,
            GetOfferByQrResponseModel(
                client.firstName,
                client.lastName,
                offer.title,
                offer.discount
            )
        )
    }
}
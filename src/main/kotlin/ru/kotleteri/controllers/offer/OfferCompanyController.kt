package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.models.base.OperationModel
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.offers.GetOfferByQrRequestModel
import ru.kotleteri.data.models.inout.offers.GetOfferByQrResponseModel
import ru.kotleteri.database.crud.ClientCRUD
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.database.crud.OfferCRUD
import ru.kotleteri.database.crud.OperationCRUD
import ru.kotleteri.database.redis.QRService
import java.time.LocalDateTime
import java.util.*

class OfferCompanyController(call: ApplicationCall) : AbstractAuthController(call) {

    init {
        if (isClient) {
            abort(HttpStatusCode.Forbidden, "You are not company")
        }
    }

    suspend fun getAllOffersByCompany() {

        val limit =
            call.parameters["limit"]?.toIntOrNull() ?: return call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Wrong limit")
            )


        val offset =
            call.parameters["offset"]?.toLongOrNull() ?: return call.respond(
                HttpStatusCode.BadRequest,
                ErrorResponse("Wrong offset")
            )


        val company = CompanyCRUD.read(id)!!

        val offersList = OfferCRUD.readByCompanyId(id, limit, offset).map {
            it.toGetOfferWithCompanyResponse(company.name)
        }

        call.respond(HttpStatusCode.OK, offersList)

    }

    suspend fun receiveOfferQr() {


        val r = call.receive<GetOfferByQrRequestModel>()

        val data = QRService.getCode(r.payload) ?: return call.respond(
            HttpStatusCode.NotFound,
            ErrorResponse("Qr is not found or expired")
        )


        val offer = OfferCRUD.read(UUID.fromString(data.offerId)) ?: return call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse("offer is null")
        )
        val client = ClientCRUD.read(UUID.fromString(data.clientId)) ?: return call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse("client is null")
        )
        val company = CompanyCRUD.read(offer.companyId) ?: return call.respond(
            HttpStatusCode.BadRequest,
            ErrorResponse("company is null")
        )

        val extension = ClientCRUD.getExtension(client.id)

        OperationCRUD.create(
            OperationModel(
                UUID.randomUUID(),
                client.id,
                company.id,
                offer.id,
                LocalDateTime.now(),
                company.name,
                offer.title,
                extension?.age,
                extension?.gender?.toString()
            )
        )


        call.respond(
            HttpStatusCode.OK,
            GetOfferByQrResponseModel(
                client.firstName,
                client.lastName,
                offer.title,
                offer.discount
            )
        )
    }
}
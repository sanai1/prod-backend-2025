package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.enums.ValidateResult
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.offers.CreateRequestModel
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.database.crud.OfferCRUD

class OfferController(call: ApplicationCall) : AbstractAuthController(call) {

    init {
        if (isClient) {
            abort(HttpStatusCode.Forbidden, "Authorized as client")
        }
    }

    suspend fun create() {
        val createRequest = call.receive<CreateRequestModel>()


        val (fieldName, result) = createRequest.validate()

        if (result != ValidateResult.Valid) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("$fieldName is $result"))
            return
        }

        val companyId = CompanyCRUD.readByEmail(email)?.id ?: return call.respond(
            HttpStatusCode.NotFound,
            ErrorResponse("Company not found")
        )

        OfferCRUD.create(createRequest.toOfferModel(companyId))

        call.respond(HttpStatusCode.Created)
    }
}
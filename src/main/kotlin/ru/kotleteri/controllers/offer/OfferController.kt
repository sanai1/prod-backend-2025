package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.enums.LoyaltyType
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

        val offer = createRequest.toOfferModel(id)

        if (offer.type == LoyaltyType.ACCUM &&
            (offer.bonusFromPurchases == null || offer.bonusPaymentPercent == null)){
            call.respond(HttpStatusCode.BadRequest,
                ErrorResponse("bonusFromPurchases and bonusPaymentPercent should not be null"))
            return
        }

        if (offer.type == LoyaltyType.DISCOUNT && offer.discount == null){
            call.respond(HttpStatusCode.BadRequest,
                ErrorResponse("discount should not be null"))
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
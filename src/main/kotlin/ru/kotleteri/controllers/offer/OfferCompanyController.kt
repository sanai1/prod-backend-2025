package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.database.crud.OfferCRUD

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
}
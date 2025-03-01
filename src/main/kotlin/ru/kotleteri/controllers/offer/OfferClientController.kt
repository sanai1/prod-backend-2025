package ru.kotleteri.controllers.offer

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.OfferCRUD

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

        val offerList = OfferCRUD.readAll(limit, offset).map{it.toGetOfferResponse()}

        call.respond(HttpStatusCode.OK, offerList)
    }
}
package ru.kotleteri.controllers.statistics

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.OperationCRUD

class StatisticsController(call: ApplicationCall): AbstractAuthController(call) {
    suspend fun getStatsByDateCompany() {
        if (isClient) {
            call.respond(HttpStatusCode.Forbidden, ErrorResponse("You are not company"))
            return
        }

        val opList = try {
            OperationCRUD.readForCompany(id)
        } catch (e: Exception){
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("wrong thing"))
            return
        }

        call.respond(HttpStatusCode.OK, opList.map { it.toResponseModel() })
    }
}
package ru.kotleteri.controllers.statistics

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.OperationCRUD

class StatisticsController(call: ApplicationCall) : AbstractAuthController(call) {

    init {
        if (isClient) {
            abort(HttpStatusCode.Forbidden, "You are not company")
        }
    }

    suspend fun getStatsByDateCompany() {

        val opList = try {
            OperationCRUD.readForCompanyByDate(id)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.localizedMessage))
            return
        }

        call.respond(HttpStatusCode.OK, opList.map { it.toResponseModel() })
    }

    suspend fun getStatsByHourCompany() {

        val opList = try {
            OperationCRUD.readForCompanyByHour(id)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.localizedMessage))
            return
        }

        call.respond(HttpStatusCode.OK, opList.map { it.toResponseModel() })
    }

    suspend fun getStatsByMonthCompany() {

        val opList = try {
            OperationCRUD.readForCompanyByMonth(id)
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.localizedMessage))
            return
        }

        call.respond(HttpStatusCode.OK, opList.map { it.toResponseModel() })
    }
}
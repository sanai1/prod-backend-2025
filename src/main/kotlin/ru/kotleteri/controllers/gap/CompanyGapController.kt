package ru.kotleteri.controllers.gap

import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.database.crud.GapCRUD

class CompanyGapController(call: ApplicationCall): AbstractAuthController(call) {
    init {
        if (isClient) {
            abort(HttpStatusCode.Forbidden, "You are not company")
        }
    }

    suspend fun getGapsList(){
        val limit = call.request.queryParameters["limit"]?.toInt() ?:
        return call.respond(HttpStatusCode.BadRequest, ErrorResponse("wrong limit"))


        val company = CompanyCRUD.read(id) ?:
        return call.respond(HttpStatusCode.NotFound, ErrorResponse("company not found"))

        val gaps = GapCRUD.getGaps(limit, company.categoryId)

        call.respond(HttpStatusCode.OK, gaps.map { it.toGetGapResponse() })
    }
}
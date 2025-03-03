package ru.kotleteri.controllers.gap

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import ru.kotleteri.controllers.AbstractAuthController
import ru.kotleteri.controllers.abort
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.gap.CreateGapModel
import ru.kotleteri.database.crud.GapCRUD

class ClientGapController(call: ApplicationCall): AbstractAuthController(call) {

    init {
        if (!isClient) {
            abort(HttpStatusCode.Forbidden, "You are not client")
        }
    }

    suspend fun addGap() {
        val r = call.receive<CreateGapModel>()

        try {
            val gap = r.toGapModel(id)
            GapCRUD.createGap(gap)
        } catch(e: Exception) {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse(e.message.toString()))
            return
        }

        call.respond(HttpStatusCode.OK)
    }

    suspend fun getGapList() {
        val limit = call.request.queryParameters["limit"]?.toInt() ?:
        return call.respond(HttpStatusCode.BadRequest, ErrorResponse("wrong limit"))

        val gaps = GapCRUD.getGapsForClient(limit, id)

        call.respond(HttpStatusCode.OK, gaps.associateBy { it.categoryId.toString() }.mapValues { (_, data) -> data.toGetGapResponse() })
    }


}

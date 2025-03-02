package ru.kotleteri.controllers.client

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.mindrot.jbcrypt.BCrypt
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.clients.LoginRequestModel
import ru.kotleteri.data.models.inout.clients.LoginResponseModel
import ru.kotleteri.data.models.inout.clients.RegisterRequestModel
import ru.kotleteri.database.crud.ClientCRUD
import ru.kotleteri.plugins.generateNewToken

class ClientController(val call: ApplicationCall) {
    suspend fun login() {
        val loginRequest = call.receive<LoginRequestModel>()

        val user = ClientCRUD.readByEmail(loginRequest.email) ?: return call.respond(
            HttpStatusCode.Unauthorized,
            ErrorResponse("User with this email and password doesn't exist")
        )


        if (!BCrypt.checkpw(loginRequest.password, user.password)) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("User with this email and password doesn't exist")
            )
            return
        }

        val token = generateNewToken(user.id, user.email, true)

        call.respond(HttpStatusCode.OK, LoginResponseModel(token))
    }

    suspend fun register() {
        val registerRequest = call.receive<RegisterRequestModel>()

        ClientCRUD.readByEmail(registerRequest.email)?.let {
            call.respond(HttpStatusCode.Conflict, ErrorResponse("User with this email already exists"))
            return
        }

        val client = registerRequest.toClientModel()

        ClientCRUD.create(client)

        val token = generateNewToken(client.id, client.email, true)

        call.respond(HttpStatusCode.OK, LoginResponseModel(token))
    }
}
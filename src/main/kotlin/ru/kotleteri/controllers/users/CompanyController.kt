package ru.kotleteri.controllers.users

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.mindrot.jbcrypt.BCrypt
import ru.kotleteri.data.enums.DatabaseStatus
import ru.kotleteri.data.enums.ValidateResult
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.data.models.inout.clients.LoginRequestModel
import ru.kotleteri.data.models.inout.clients.LoginResponseModel
import ru.kotleteri.data.models.inout.clients.RegisterCompanyRequestModel
import ru.kotleteri.database.crud.CompanyCRUD
import ru.kotleteri.plugins.generateNewToken

class CompanyController(val call: ApplicationCall) {
    suspend fun login(){
        val loginRequest = call.receive<LoginRequestModel>()

        val user = CompanyCRUD.readByEmail(loginRequest.email)

        if (user == null) {
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("User with this email and password doesn't exist")
            )
            return
        }

        if (!BCrypt.checkpw(loginRequest.password, user.password)){
            call.respond(
                HttpStatusCode.Unauthorized,
                ErrorResponse("User with this email and password doesn't exist")
            )
            return
        }

        val token = generateNewToken(user.id, user.email, false)

        call.respond(HttpStatusCode.OK, LoginResponseModel(token))
    }

    suspend fun register() {
        val registerRequest = call.receive<RegisterCompanyRequestModel>()

        val (fieldName, result) = registerRequest.validate()

        if (result != ValidateResult.Valid){
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("$fieldName is $result"))
            return
        }

        val company = registerRequest.toCompanyModel()

        val status = CompanyCRUD.create(company)

        if (status != DatabaseStatus.Correct){
            call.respond(HttpStatusCode.Conflict, ErrorResponse("User with this email already exists"))
            return
        }

        val token = generateNewToken(company.id, company.email, false)

        call.respond(HttpStatusCode.OK, LoginResponseModel(token))
    }
}
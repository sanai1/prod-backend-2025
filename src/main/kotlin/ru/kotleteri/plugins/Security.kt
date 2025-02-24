package ru.kotleteri.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import ru.kotleteri.data.models.inout.ErrorResponse
import ru.kotleteri.utils.SECRET
import java.util.*

fun Application.configureSecurity() {
    install(Authentication) {
        jwt {
            verifier(JWT.require(Algorithm.HMAC256(SECRET)).build())
            validate { credential ->
                val email = credential.payload.getClaim("email").asString()
                val id = credential.payload.getClaim("id").asInt()

                if (!email.isNullOrBlank())
                    return@validate JWTPrincipal(credential.payload)
                return@validate null

            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, ErrorResponse("Token is not valid or has expired"))
            }
        }
    }
}

fun generateNewToken(id: Int, email: String): String =
    JWT.create()
        .withClaim("email", email)
        .withClaim("id", id)
        .withExpiresAt(Date(System.currentTimeMillis() + 1000L * 60L * 60L * 23L))
        .sign(Algorithm.HMAC256(SECRET))

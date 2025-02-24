package ru.kotleteri.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import ru.kotleteri.controllers.jokes.JokeAuthController
import ru.kotleteri.controllers.users.UserController

fun Application.configureRouting() {
    routing {
        route("/api"){
            post("/register") {
                UserController(call).register()
            }

            post("/login") {
                UserController(call).login()
            }

            authenticate {
                get("/joke"){
                    JokeAuthController(call).getJoke()
                }
            }

            swaggerUI(
                path = "/docs",
                swaggerFile = "openapi/documentation.yaml"
            )
        }
    }
}

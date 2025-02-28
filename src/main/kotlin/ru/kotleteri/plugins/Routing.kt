package ru.kotleteri.plugins

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*
import ru.kotleteri.controllers.users.ClientController
import ru.kotleteri.controllers.users.CompanyController

fun Application.configureRouting() {
    routing {
        route("/api"){

            route("/clients"){
                post("/register"){
                    ClientController(call).register()
                }
                post("/login"){
                    ClientController(call).login()
                }
            }

            route("/companies"){
                post("/register"){
                    CompanyController(call).register()
                }
                post("/login"){
                    CompanyController(call).login()
                }
            }

            authenticate {

            }

            swaggerUI(
                path = "/docs",
                swaggerFile = "openapi/documentation.yaml"
            )
        }
    }
}

package ru.kotleteri.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import ru.kotleteri.controllers.offer.OfferClientController
import ru.kotleteri.controllers.offer.OfferController
import ru.kotleteri.controllers.client.ClientController
import ru.kotleteri.controllers.company.AuthCompanyController
import ru.kotleteri.controllers.company.CompanyController
import ru.kotleteri.controllers.offer.OfferCompanyController

fun Application.configureRouting() {
    routing {
        route("/api"){

            get("/ping"){
                call.respond(HttpStatusCode.OK)
            }

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
                route("/offers") {
                    post("/create") {
                        OfferController(call).create()
                    }
                    route("/client"){
                        get {
                            OfferClientController(call).getOffersList()
                        }
                        post("/generateQr") {
                            OfferClientController(call).generateQrPayload()
                        }
                    }
                    route("/company"){
                        get {
                            OfferCompanyController(call).getAllOffersByCompany()
                        }

                        post("/scanQr") {
                            OfferCompanyController(call).receiveOfferQr()
                        }

                        route("/image"){
                            post {
                                AuthCompanyController(call).setPicture()
                            }
                            get {
                                AuthCompanyController(call).getPicture()
                            }
                        }
                    }
                }
            }

            swaggerUI(
                path = "/docs",
                swaggerFile = "openapi/documentation.yaml"
            )
        }
    }
}

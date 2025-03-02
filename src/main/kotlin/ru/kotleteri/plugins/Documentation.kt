package ru.kotleteri.plugins

import io.bkbn.kompendium.core.plugin.NotarizedApplication
import io.bkbn.kompendium.oas.OpenApiSpec
import io.bkbn.kompendium.oas.component.Components
import io.bkbn.kompendium.oas.info.Info
import io.bkbn.kompendium.oas.security.BearerAuth
import io.ktor.server.application.*

fun Application.configureDocumentation() {
    install(NotarizedApplication()) {
        spec = {
            OpenApiSpec(
                info = Info(
                    title = "Offeria",
                    version = "1.0.0",
                    description = "Tipa opisanie"
                ),
                components = Components(
                    securitySchemes = mutableMapOf(
                        "jwt" to BearerAuth("JWT")
                    )
                )
            )
        }
    }
}
package ru.kotleteri.plugins

import io.ktor.server.html.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.request.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlinx.html.*

fun Route.swaggerUI(
    path: String,
    apiUrl: String,
    vararg tokens: Pair<String, suspend () -> String>,
    block: SwaggerConfig.() -> Unit = {}
) {
    val config = SwaggerConfig().apply(block)

    route(path) {
        get {
            val fullPath = call.request.path()
            val docExpansion = runCatching {
                call.request.queryParameters.getOrFail<String>("docExpansion")
            }.getOrNull()
            val tokenData = listOf(*tokens).associate { (name, token) -> name to token.invoke() }
            call.respondHtml {
                head {
                    title { +"Swagger UI" }
                    link(
                        href = "${config.packageLocation}@${config.version}/swagger-ui.css",
                        rel = "stylesheet"
                    )
                    link(
                        href = config.faviconLocation,
                        rel = "icon",
                        type = "image/x-icon"
                    )
                }
                body {
                    div { id = "swagger-ui" }
                    script(src = "${config.packageLocation}@${config.version}/swagger-ui-bundle.js") {
                        attributes["crossorigin"] = "anonymous"
                    }

                    val src = "${config.packageLocation}@${config.version}/swagger-ui-standalone-preset.js"
                    script(src = src) {
                        attributes["crossorigin"] = "anonymous"
                    }

                    script {
                        unsafe {
                            +"""
window.onload = function() {
    window.ui = SwaggerUIBundle({
        url: '$apiUrl',
        dom_id: '#swagger-ui',
        deepLinking: ${config.deepLinking},
        presets: [
            SwaggerUIBundle.presets.apis,
            SwaggerUIStandalonePreset
        ],
        layout: 'StandaloneLayout'${docExpansion?.let { ",\n        docExpansion: '$it'" } ?: ""},
        onComplete: () => {
            ${tokenData.map { (name, token) -> "ui.preauthorizeApiKey(\"$name\", \"$token\")" }.joinToString("\n") }
        }
    });
}
                            """.trimIndent()
                        }
                    }
                }
            }
        }
    }
}

package ru.kotleteri.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*
import ru.kotleteri.data.enums.ValidateResult
import ru.kotleteri.utils.Validateable

fun Application.configureValidation() {
    install(RequestValidation) {
        validate<Validateable> {
            val (field, result) = it.validate()
            if (result == ValidateResult.Valid) ValidationResult.Valid
            else ValidationResult.Invalid("$field is $result")
        }
    }
}
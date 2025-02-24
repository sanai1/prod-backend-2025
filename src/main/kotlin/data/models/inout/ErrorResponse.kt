package ru.kotleteri.data.models.inout

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val status: String
)

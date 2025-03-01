package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable

@Serializable
data class GenerateQRPayloadResponseModel(
    val payload: String
)

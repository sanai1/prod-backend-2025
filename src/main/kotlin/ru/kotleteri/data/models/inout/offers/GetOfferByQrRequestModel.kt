package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable

@Serializable
data class GetOfferByQrRequestModel(
    val payload: String,
    val cost: Double
)

package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable

@Serializable
data class GetOfferByQrResponseModel(
    val client_first_name: String,
    val client_last_name: String,
    val offer_title: String,
    val offer_discount: Double
)

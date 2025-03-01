package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable

@Serializable
data class GetOfferResponseModel(
    val id: String,
    val company_id: String,
    val company_name: String,
    val title: String,
    val description: String,
    val discount: Double,
    val start_date: String,
    val end_date: String,
)

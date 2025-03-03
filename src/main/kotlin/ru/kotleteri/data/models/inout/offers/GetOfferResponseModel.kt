package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable

@Serializable
data class GetOfferResponseModel(
    val id: String,
    val type: String,
    val company_id: String,
    val company_name: String,
    val title: String,
    val description: String,
    val start_date: String,
    val end_date: String,
    val discount: Double? = null,
    val bonus_from_purchases: Double? = null,
    val bonus_payment_percent: Double? = null,
)

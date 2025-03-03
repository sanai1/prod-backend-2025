package ru.kotleteri.data.models.redis

import kotlinx.serialization.Serializable

@Serializable
data class QRDataModel(
    val clientId: String,
    val offerId: String,
    val spendBonus: Boolean
)

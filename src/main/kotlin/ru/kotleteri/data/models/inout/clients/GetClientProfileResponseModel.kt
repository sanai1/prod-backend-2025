package ru.kotleteri.data.models.inout.clients

import kotlinx.serialization.Serializable

@Serializable
data class GetClientProfileResponseModel(
    val id: String,
    val first_name: String,
    val last_name: String,
    val email: String,
)
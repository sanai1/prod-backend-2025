package ru.kotleteri.data.models.inout.clients

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel(
    val token: String
)
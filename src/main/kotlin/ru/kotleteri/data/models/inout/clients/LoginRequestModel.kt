package ru.kotleteri.data.models.inout.clients

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestModel(
    val email: String,
    val password: String
)

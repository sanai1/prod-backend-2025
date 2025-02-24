package ru.kotleteri.data.models.inout.users

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestModel(
    val email: String,
    val password: String
)

package ru.kotleteri.data.models.inout.users

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponseModel (
    val token: String
)
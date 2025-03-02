package ru.kotleteri.data.models.inout.clients

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequestModel(
    var email: String,
    val password: String
) {
    init {
        email = email.lowercase()
    }
}

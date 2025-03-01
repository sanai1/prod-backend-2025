package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.clients.GetClientProfileResponseModel
import java.util.*

data class ClientModel(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
) {
    fun getProfile() =
        GetClientProfileResponseModel(
            id.toString(),
            firstName,
            lastName,
            email
        )
}
package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.clients.GetClientProfileResponseModel
import java.util.*

data class ClientModel(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String,
    val bonus: Double = 0.0
) {
    fun getProfile(target: ClientExtensionModel?) =
        GetClientProfileResponseModel(
            id.toString(),
            firstName,
            lastName,
            email,
            bonus,
            target?.age,
            target?.gender?.toString()
        )
}
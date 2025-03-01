package ru.kotleteri.data.models.base

import java.util.*

data class ClientModel(
    val id: UUID,
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String
)
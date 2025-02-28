package ru.kotleteri.data.models.base

import java.util.UUID

data class CompanyModel(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String
)

package ru.kotleteri.data.models.base

import java.util.*

data class CompanyModel(
    val id: UUID,
    val name: String,
    val email: String,
    val password: String
)

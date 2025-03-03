package ru.kotleteri.data.models.base

import java.util.UUID

data class GapModel(
    val clientId: UUID,
    val message: String,
    val averageSpent: Double,
    val categoryId: Int
)

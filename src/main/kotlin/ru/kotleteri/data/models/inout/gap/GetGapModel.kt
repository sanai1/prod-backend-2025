package ru.kotleteri.data.models.inout.gap

import kotlinx.serialization.Serializable

@Serializable
data class GetGapModel(
    val message: String,
    val averageSpent: Double
)

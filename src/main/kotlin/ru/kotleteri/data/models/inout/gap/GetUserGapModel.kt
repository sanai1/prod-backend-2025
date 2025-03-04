package ru.kotleteri.data.models.inout.gap

import kotlinx.serialization.Serializable

@Serializable
data class GetUserGapModel(
    val message: String,
    val categoryName: String,
    val subcategoryName: String
)
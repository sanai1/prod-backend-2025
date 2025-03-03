package ru.kotleteri.data.models.inout.companies

import kotlinx.serialization.Serializable

@Serializable
data class GetCategoryResponseModel(
    val id: Int,
    val category: String,
    val subcategory: String
)

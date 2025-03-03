package ru.kotleteri.data.models.inout.companies

import kotlinx.serialization.Serializable

@Serializable
data class GetCompanyProfileResponseModel(
    val id: String,
    val name: String,
    val email: String,
    val category_name: String,
    val subcategory_name: String,
    val category_id: Int
)

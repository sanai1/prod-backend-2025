package ru.kotleteri.data.models.inout.companies

import kotlinx.serialization.Serializable

@Serializable
data class GetCompanyProfileResponseModel(
    val id: String,
    val name: String,
    val email: String,
)

package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.companies.GetCompanyProfileResponseModel
import java.util.*

data class CompanyModel(
    val id: UUID,
    val name: String,
    val email: String,
    val categoryId: Int,
    val password: String
) {
    fun getProfile(cat: CategoryModel) =
        GetCompanyProfileResponseModel(
            id.toString(),
            name,
            email,
            cat.name,
            cat.subname,
            cat.id
        )
}

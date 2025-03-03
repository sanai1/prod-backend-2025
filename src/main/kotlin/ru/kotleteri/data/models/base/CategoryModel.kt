package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.companies.GetCategoryResponseModel

data class CategoryModel(
    val id: Int,
    val name: String,
    val subname: String
) {
    fun toResponseModel() =
        GetCategoryResponseModel(
            id,
            name,
            subname
        )
}

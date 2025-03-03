package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.gap.GetGapModel
import ru.kotleteri.data.models.inout.gap.GetUserGapModel
import java.util.*

data class GapModel(
    val clientId: UUID,
    val message: String,
    val averageSpent: Double,
    val categoryId: Int
) {
    fun toGetGapResponse() =
        GetGapModel(
            message,
            averageSpent
        )

    fun toGetUserGapResponse(categoryName: String, subCategoryName: String) =
        GetUserGapModel(
            message,
            categoryName,
            subCategoryName
        )
}

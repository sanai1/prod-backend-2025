package ru.kotleteri.data.models.inout.gap

import kotlinx.serialization.Serializable
import ru.kotleteri.data.models.base.GapModel
import java.util.*

@Serializable
data class CreateGapModel(
    val message: String,
    val categoryId: Int,
    val averageSpent: Double
) {
    fun toGapModel(id: UUID) =
        GapModel(
            id,
            message,
            averageSpent,
            categoryId
        )
}

package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable
import ru.kotleteri.data.models.base.OfferModel
import ru.kotleteri.utils.Validateable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class CreateRequestModel(
    val title: String,
    val description: String,
    val discount: Double,
    val startDate: String,
    val endDate: String
) : Validateable {
    override fun performValidation() {

    }

    fun toOfferModel(companyId: UUID) = OfferModel(
        UUID.randomUUID(),
        companyId,
        title,
        description,
        discount,
        LocalDateTime.parse(startDate),
        LocalDateTime.parse(endDate)
    )
}

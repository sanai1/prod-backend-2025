package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable
import ru.kotleteri.data.models.base.OfferModel
import ru.kotleteri.utils.Validate
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
        Validate.string(this::title, 1..50)
        Validate.string(this::description, 1..500)
        Validate.number(this::discount, 0.0..100.0)
        Validate.custom(this::startDate) {
            try {
                LocalDateTime.parse(it)
                true
            } catch (e: Exception) {
                false
            }
        }
        Validate.custom(this::endDate) {
            try {
                LocalDateTime.parse(it)
                true
            } catch (e: Exception) {
                false
            }
        }
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

package ru.kotleteri.data.models.inout.offers

import kotlinx.serialization.Serializable
import ru.kotleteri.data.enums.LoyaltyType
import ru.kotleteri.data.models.base.OfferModel
import ru.kotleteri.utils.Validate
import ru.kotleteri.utils.Validateable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class CreateRequestModel(
    val type: String,
    val title: String,
    val description: String,
    val start_date: String,
    val end_date: String,
    val discount: Double? = null,
    val bonus_from_purchases: Double? = null,
    val bonus_payment_percent: Double? = null,
) : Validateable {
    override fun performValidation() {
        Validate.string(this::title, 1..50)
        Validate.string(this::description, 1..500)
        Validate.number(this::discount, 0.0..100.0, ignoreNull = true)
        Validate.number(this::bonus_from_purchases, 0.0..100.0, ignoreNull = true)
        Validate.number(this::bonus_payment_percent, 0.0..100.0, ignoreNull = true)
        Validate.custom(this::start_date) {
            try {
                LocalDateTime.parse(it)
                true
            } catch (e: Exception) {
                false
            }
        }
        Validate.custom(this::end_date) {
            try {
                LocalDateTime.parse(it)
                true
            } catch (e: Exception) {
                false
            }
        }

        Validate.custom(this::type){
            when (it.lowercase()){
                "discount" -> true
                "stamp" -> true
                "accum" -> true
                else -> false
            }
        }
    }

    fun toLoyaltyEnum(str: String) = when (str.lowercase()){
        "accum" -> LoyaltyType.ACCUM
        else -> LoyaltyType.DISCOUNT
    }

    fun toOfferModel(companyId: UUID) = OfferModel(
        UUID.randomUUID(),
        toLoyaltyEnum(type),
        companyId,
        title,
        description,
        LocalDateTime.parse(start_date),
        LocalDateTime.parse(end_date),
        discount,
        bonus_from_purchases,
        bonus_payment_percent,
    )
}

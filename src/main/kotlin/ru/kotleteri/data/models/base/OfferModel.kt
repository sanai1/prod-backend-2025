package ru.kotleteri.data.models.base

import ru.kotleteri.data.enums.LoyaltyType
import ru.kotleteri.data.models.inout.offers.GetOfferResponseModel
import java.time.LocalDateTime
import java.util.*

data class OfferModel(
    val id: UUID,
    val type: LoyaltyType,
    val companyId: UUID,
    val title: String,
    val description: String,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime,
    val discount: Double? = null,
    val bonusFromPurchases: Double? = null,
    val bonusPaymentPercent: Double? = null,
) {

    fun toGetOfferWithCompanyResponse(companyName: String) =
        GetOfferResponseModel(
            id.toString(),
            type.toString(),
            companyId.toString(),
            companyName,
            title,
            description,
            startDate.toString(),
            endDate.toString(),
            discount,
            bonusFromPurchases,
            bonusPaymentPercent
        )
}
package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.offers.GetOfferResponseModel
import java.time.LocalDateTime
import java.util.UUID

data class OfferModel(
    val id: UUID,
    val companyId: UUID,
    val title: String,
    val description: String,
    val discount: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
) {

    fun toGetOfferWithCompanyResponse(companyName: String) =
        GetOfferResponseModel(
            id.toString(),
            companyId.toString(),
            companyName,
            title,
            description,
            discount,
            startDate.toString(),
            endDate.toString()
        )
}
package ru.kotleteri.database.crud

import org.jetbrains.exposed.sql.ResultRow
import ru.kotleteri.data.models.base.OfferModel
import ru.kotleteri.database.tables.OfferTable

object OfferCRUD {
    fun resultRowToOffer(resultRow: ResultRow): OfferModel =
        OfferModel(
            id = resultRow[OfferTable.id].value,
            companyId = resultRow[OfferTable.companyId].value,
            title = resultRow[OfferTable.title],
            description = resultRow[OfferTable.description],
            discount = resultRow[OfferTable.discount],
            startDate = resultRow[OfferTable.startDate],
            endDate = resultRow[OfferTable.endDate]
        )
}
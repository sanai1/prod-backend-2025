package ru.kotleteri.database.crud

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.kotleteri.data.models.base.OfferModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.OfferTable
import java.util.*

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

    suspend fun create(offer: OfferModel) =
        suspendTransaction {
            OfferTable.insert {
                it[id] = offer.id
                it[companyId] = offer.companyId
                it[title] = offer.title
                it[description] = offer.description
                it[discount] = offer.discount
                it[startDate] = offer.startDate
                it[endDate] = offer.endDate
            }
        }

    suspend fun read(id: UUID) =
        suspendTransaction {
            OfferTable.selectAll()
                .where { OfferTable.id eq id }
                .singleOrNull()
                ?.let { resultRowToOffer(it) }
        }

    suspend fun readByCompanyId(companyId: UUID) =
        suspendTransaction {
            OfferTable.selectAll()
                .where { OfferTable.companyId eq companyId }
                .map { resultRowToOffer(it) }
        }

    suspend fun readAll(limit: Int, offset: Long) = suspendTransaction {
        OfferTable.selectAll()
            .offset(offset)
            .limit(limit)
            .map(::resultRowToOffer)
    }

    suspend fun update(offer: OfferModel) =
        suspendTransaction {
            OfferTable.update({ OfferTable.id eq offer.id }) {
                it[companyId] = offer.companyId
                it[title] = offer.title
                it[description] = offer.description
                it[discount] = offer.discount
                it[startDate] = offer.startDate
                it[endDate] = offer.endDate
            }
        }

    suspend fun delete(id: UUID) =
        suspendTransaction {
            OfferTable.deleteWhere { OfferTable.id eq id }
        }
}
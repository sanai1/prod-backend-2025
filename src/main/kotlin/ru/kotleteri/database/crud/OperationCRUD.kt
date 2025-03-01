package ru.kotleteri.database.crud

import org.jetbrains.exposed.sql.*
import ru.kotleteri.data.models.base.OperationModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.OfferTable
import ru.kotleteri.database.tables.OperationTable
import java.util.*

object OperationCRUD {
    fun resultRowToOperation(resultRow: ResultRow): OperationModel =
        OperationModel(
            id = resultRow[OperationTable.id].value,
            clientId = resultRow[OperationTable.clientId],
            offerId = resultRow[OperationTable.offerId],
            timestamp = resultRow[OperationTable.timestamp],
        )

    suspend fun create(operation: OperationModel) = suspendTransaction {
        OperationTable.insert {
            it[id] = operation.id
            it[clientId] = operation.clientId
            it[offerId] = operation.offerId
            it[timestamp] = operation.timestamp
        }
    }

    suspend fun readForClient(clientId: UUID, limit: Int, offset: Long): List<OperationModel> = suspendTransaction {
        OperationTable.selectAll()
            .where { OperationTable.clientId eq clientId }
            .limit(limit)
            .offset(offset)
            .map { resultRowToOperation(it) }
    }

    suspend fun readForCompany(companyId: UUID, limit: Int, offset: Long): List<OperationModel> = suspendTransaction {
        OperationTable.rightJoin(OfferTable, onColumn = { OperationTable.offerId }, otherColumn = { OfferTable.id } )
            .selectAll()
            .where { OfferTable.companyId eq companyId }
            .limit(limit)
            .offset(offset)
            .map { resultRowToOperation(it) }
    }

}
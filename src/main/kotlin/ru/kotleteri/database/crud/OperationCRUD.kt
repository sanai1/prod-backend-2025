package ru.kotleteri.database.crud

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ru.kotleteri.data.models.base.OperationModel
import ru.kotleteri.data.models.base.StatisticsByDate
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.OperationTable
import ru.kotleteri.utils.StatementPrepare
import java.time.LocalDate
import java.util.*

object OperationCRUD {

    private val selectByDateExpression = """
        SELECT cast(operations.timestamp as DATE) date,
               count(*) allops,
               count(*) filter ( where operations.client_gender = 'MALE' ) maleops,
               count(*) filter ( where operations.client_gender = 'FEMALE' ) femaleops
               FROM operations
               where operations.company_id = ?
        group by cast(operations.timestamp as DATE)
    """.trimIndent()

    fun resultRowToOperation(resultRow: ResultRow): OperationModel =
        OperationModel(
            id = resultRow[OperationTable.id].value,
            clientId = resultRow[OperationTable.clientId],
            companyId = resultRow[OperationTable.companyId],
            offerId = resultRow[OperationTable.offerId],
            timestamp = resultRow[OperationTable.timestamp],
            companyName = resultRow[OperationTable.companyName],
            offerTitle = resultRow[OperationTable.offerTitle],
            clientAge = resultRow[OperationTable.clientAge],
            clientGender = resultRow[OperationTable.clientGender]
        )

    suspend fun create(operation: OperationModel) = suspendTransaction {
        OperationTable.insert {
            it[id] = operation.id
            it[clientId] = operation.clientId
            it[companyId] = operation.companyId
            it[offerId] = operation.offerId
            it[timestamp] = operation.timestamp
            it[companyName] = operation.companyName
            it[offerTitle] = operation.offerTitle
            it[clientAge] = operation.clientAge
            it[clientGender] = operation.clientGender
        }
    }

    suspend fun readForClient(clientId: UUID) = suspendTransaction {
        OperationTable.selectAll()
            .where { OperationTable.clientId eq clientId }
            .map { resultRowToOperation(it) }
    }

    suspend fun readForCompany(companyId: UUID) = suspendTransaction {
        val statement =
            StatementPrepare(selectByDateExpression)
                .addParam(companyId.toString())
                .build()

        val stats = mutableListOf<StatisticsByDate>()

        exec(statement) { rs ->
            while (rs.next()) {
                stats.add(
                    StatisticsByDate(
                        rs.getDate("date") as LocalDate,
                        rs.getInt("allops"),
                        rs.getInt("maleops"),
                        rs.getInt("femaleops"),
                    )
                )
            }
        }

        return@suspendTransaction stats.toList()
    }

}
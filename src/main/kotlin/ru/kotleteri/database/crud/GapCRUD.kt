package ru.kotleteri.database.crud

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ru.kotleteri.data.models.base.GapModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.GapTable
import java.util.*

object GapCRUD {
    fun resultRowToGap(resultRow: ResultRow) =
        GapModel(
            resultRow[GapTable.clientId].value,
            resultRow[GapTable.message],
            resultRow[GapTable.averageSpent],
            resultRow[GapTable.categoryId].value
        )

    suspend fun getGaps(limit: Int, categoryId: Int) = suspendTransaction {
        GapTable.selectAll()
            .where { GapTable.categoryId eq categoryId }
            .orderBy( GapTable.id to SortOrder.DESC )
            .limit(limit)
            .map(::resultRowToGap)
    }

    suspend fun getGapsForClient(limit: Int, clientId: UUID) = suspendTransaction {
        GapTable.selectAll()
            .where { GapTable.clientId eq clientId }
            .orderBy( GapTable.id to SortOrder.DESC )
            .limit(limit)
            .map(::resultRowToGap)
    }

    suspend fun createGap(gap: GapModel) = suspendTransaction {
        GapTable.insert {
            it[GapTable.clientId] = gap.clientId
            it[GapTable.message] = gap.message
            it[GapTable.averageSpent] = gap.averageSpent
            it[GapTable.categoryId] = gap.categoryId
        }
    }
}
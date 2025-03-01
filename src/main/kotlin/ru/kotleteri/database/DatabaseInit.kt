package ru.kotleteri.database

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kotleteri.database.tables.*

object DatabaseInit {
    private val tables: List<Table> =
        listOf(
            ClientTable,
            CompanyTable,
            OfferTable,
            OperationTable,
            ClientExtensionTable
        )

    fun initialize() {
        transaction {
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
        }
    }
}
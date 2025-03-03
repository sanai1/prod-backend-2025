package ru.kotleteri.database

import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kotleteri.database.crud.CategoryCRUD
import ru.kotleteri.database.tables.*

object DatabaseInit {
    val tables: List<Table> =
        listOf(
            ClientTable,
            CompanyTable,
            OfferTable,
            OperationTable,
            ClientExtensionTable,
            CategoryTable,
            GapTable
        )

    fun initialize() {
        transaction {
            SchemaUtils.create(*tables.toTypedArray())
            SchemaUtils.createMissingTablesAndColumns(*tables.toTypedArray())
            commit()
        }
        runBlocking {
            if (!CategoryCRUD.isAnyCategoryExists()) {
                CategoryCRUD.create(categoriesToCreate)
            }
        }
    }
}
package ru.kotleteri.database

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kotleteri.database.tables.UsersTable

object DatabaseInit {
    private val tables: List<Table> =
        listOf(
            UsersTable
        )

    fun initialize() {
        transaction {
            tables.forEach {
                SchemaUtils.create(it)
            }
        }
    }
}
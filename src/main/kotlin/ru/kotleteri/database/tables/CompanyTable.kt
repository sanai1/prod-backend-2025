package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import ru.kotleteri.database.tables.ClientTable.entityId
import ru.kotleteri.database.tables.ClientTable.uniqueIndex
import java.util.UUID

object CompanyTable: IdTable<UUID> ("company") {
    override val id = ClientTable.uuid("id").uniqueIndex().entityId()
    val name = varchar("name", 50)
    val email = ClientTable.varchar("email", 128)
    val password = UsersTable.varchar("password", 60).uniqueIndex()
}
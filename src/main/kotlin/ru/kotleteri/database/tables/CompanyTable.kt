package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import java.util.*

object CompanyTable: IdTable<UUID> ("company") {
    override val id = uuid("id").uniqueIndex().entityId()
    val name = varchar("name", 50)
    val email = varchar("email", 128)
    val password = varchar("password", 60)
}
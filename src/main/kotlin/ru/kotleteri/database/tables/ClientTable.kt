package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import java.util.*

object ClientTable : IdTable<UUID>("clients") {
    override val id = uuid("id").uniqueIndex().entityId()
    val firstName = varchar("first_name", 50)
    val lastName = varchar("last_name", 50)
    val email = varchar("email", 128).uniqueIndex()
    val password = varchar("password", 60)
    val bonus = double("bonus").default(0.0)
}
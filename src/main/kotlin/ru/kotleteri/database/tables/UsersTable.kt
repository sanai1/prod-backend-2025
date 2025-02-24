package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object UsersTable: IntIdTable("users") {
    val email = varchar("email", 128).uniqueIndex()
    val firstName = varchar("first_name", 32).uniqueIndex()
    val lastName = varchar("last_name", 32).uniqueIndex()
    val password = varchar("password", 60).uniqueIndex()
}
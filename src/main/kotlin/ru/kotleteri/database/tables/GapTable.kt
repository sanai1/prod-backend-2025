package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object GapTable : IntIdTable("gaps") {
    val clientId = reference("client_id", ClientTable.id, onDelete = ReferenceOption.CASCADE)
    val message = varchar("message", 1000)
    val categoryId = reference("category_id", CategoryTable.id)
    val averageSpent = double("average_spent")
}
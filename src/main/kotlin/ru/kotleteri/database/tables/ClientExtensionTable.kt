package ru.kotleteri.database.tables

import org.jetbrains.exposed.sql.Table
import ru.kotleteri.data.enums.Gender

object ClientExtensionTable : Table("client_extensions") {
    val clientId = reference("client_id", ClientTable.id)
    val age = integer("age")
    val gender = enumerationByName<Gender>("gender", 6)
}
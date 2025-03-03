package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IntIdTable

object CategoryTable: IntIdTable("categories") {
    val categoryName = varchar("category_name", 50)
    val categorySub = varchar("subcategory_name", 50)
}
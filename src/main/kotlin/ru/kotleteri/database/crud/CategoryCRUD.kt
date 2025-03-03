package ru.kotleteri.database.crud

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import ru.kotleteri.data.models.base.CategoryModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.CategoryTable

object CategoryCRUD {
    private fun resultRowToCategory(resultRow: ResultRow) =
        CategoryModel(
            resultRow[CategoryTable.id].value,
            resultRow[CategoryTable.categoryName],
            resultRow[CategoryTable.categorySub]
        )

    suspend fun create(categories: List<Pair<String, String>>) = suspendTransaction {
        categories.forEach { (name, subname) ->
            CategoryTable.insert {
                it[CategoryTable.categoryName] = name
                it[CategoryTable.categorySub] = subname
            }
        }
    }

    suspend fun isAnyCategoryExists() = suspendTransaction {
        CategoryTable.selectAll().count() > 0
    }

    suspend fun getAllCategories() = suspendTransaction {
        CategoryTable.selectAll()
            .map(::resultRowToCategory)
    }

    suspend fun read(id: Int) = suspendTransaction {
        CategoryTable.selectAll()
            .where { CategoryTable.id eq id }
            .singleOrNull()
            ?.let(::resultRowToCategory)
    }
}
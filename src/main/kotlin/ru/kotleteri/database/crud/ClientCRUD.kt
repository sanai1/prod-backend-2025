package ru.kotleteri.database.crud

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.kotleteri.data.enums.DatabaseStatus
import ru.kotleteri.data.enums.Gender
import ru.kotleteri.data.models.base.ClientModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.ClientExtensionTable
import ru.kotleteri.database.tables.ClientTable
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*

object ClientCRUD {
    private fun resultRowToClientModel(resultRow: ResultRow) =
        ClientModel(
            resultRow[ClientTable.id].value,
            resultRow[ClientTable.firstName],
            resultRow[ClientTable.lastName],
            resultRow[ClientTable.email],
            resultRow[ClientTable.password]
        )

    suspend fun create(user: ClientModel): DatabaseStatus = suspendTransaction {
        try {
            ClientTable.insert {
                it[id] = user.id
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[email] = user.email
                it[password] = user.password
            }
            return@suspendTransaction DatabaseStatus.Correct
        } catch (ex: ExposedSQLException) {
            val cause = ex.cause
            return@suspendTransaction when (cause) {
                is SQLIntegrityConstraintViolationException ->
                    DatabaseStatus.ConstraintViolation

                else ->
                    DatabaseStatus.Incorrect
            }
        }

    }

    suspend fun read(id: UUID) = suspendTransaction {
        ClientTable.selectAll()
            .where { ClientTable.id eq id }
            .singleOrNull()
            .let {
                if (it == null) null
                else resultRowToClientModel(it)
            }
    }

    suspend fun readByEmail(email: String) = suspendTransaction {
        ClientTable.selectAll()
            .where { ClientTable.email eq email }
            .singleOrNull()
            .let {
                if (it == null) null
                else resultRowToClientModel(it)
            }
    }

    suspend fun update(user: ClientTable): DatabaseStatus = suspendTransaction {
        try {
            ClientTable.update({ ClientTable.id eq user.id }) {
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[email] = user.email
            }
            return@suspendTransaction DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            val cause = e.cause
            return@suspendTransaction when (cause) {
                is SQLIntegrityConstraintViolationException ->
                    DatabaseStatus.ConstraintViolation

                else ->
                    DatabaseStatus.Incorrect
            }
        }
    }

    suspend fun updatePassword(user: ClientModel): DatabaseStatus = suspendTransaction {
        try {
            ClientTable.update({ ClientTable.id eq user.id }) {
                it[password] = user.password
            }
            return@suspendTransaction DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            return@suspendTransaction DatabaseStatus.Incorrect
        }
    }

    suspend fun delete(id: UUID) = suspendTransaction {
        ClientTable.deleteWhere { ClientTable.id eq id }
    }

    suspend fun addExtension(clientId: UUID, age: Int, gender: Gender) = suspendTransaction {
        ClientExtensionTable.insert {
            it[ClientExtensionTable.clientId] = clientId
            it[ClientExtensionTable.age] = age
            it[ClientExtensionTable.gender] = gender
        }
    }
}
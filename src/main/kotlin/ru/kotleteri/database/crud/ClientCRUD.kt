package ru.kotleteri.database.crud

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kotleteri.data.enums.DatabaseStatus
import ru.kotleteri.data.models.base.ClientModel
import ru.kotleteri.data.models.base.UserModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.ClientTable
import ru.kotleteri.database.tables.UsersTable
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

    suspend fun create(user: ClientModel): DatabaseStatus {
        try {
            suspendTransaction {
                ClientTable.insert {
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[email] = user.email
                    it[password] = user.password
                }
            }
            return DatabaseStatus.Correct
        } catch (ex: ExposedSQLException) {
            val cause = ex.cause
            return when (cause) {
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
            .let { if(it == null) null
            else resultRowToClientModel(it) }
    }

    suspend fun readByEmail(email: String) = suspendTransaction {
        ClientTable.selectAll()
            .where { ClientTable.email eq email }
            .singleOrNull()
            .let { if(it == null) null
            else resultRowToClientModel(it) }
    }

    suspend fun update(user: ClientTable): DatabaseStatus {
        try {
            suspendTransaction {
                ClientTable.update({ ClientTable.id eq user.id }) {
                    it[firstName] = user.firstName
                    it[lastName] = user.lastName
                    it[email] = user.email
                }
            }
            return DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            val cause = e.cause
            return when (cause) {
                is SQLIntegrityConstraintViolationException ->
                    DatabaseStatus.ConstraintViolation
                else ->
                    DatabaseStatus.Incorrect
            }
        }
    }

    suspend fun updatePassword(user: ClientModel): DatabaseStatus {
        try {
            suspendTransaction {
                ClientTable.update({ ClientTable.id eq user.id }) {
                    it[password] = user.password
                }
            }
            return DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            return DatabaseStatus.Incorrect
        }
    }

    suspend fun delete(id: UUID) = suspendTransaction {
        ClientTable.deleteWhere { ClientTable.id eq id }
    }
}
package ru.kotleteri.database.crud

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kotleteri.data.enums.DatabaseStatus
import ru.kotleteri.data.models.base.UserModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.UsersTable
import java.sql.SQLIntegrityConstraintViolationException

object UsersCRUD {
    private fun resultRowToUserModel(resultRow: ResultRow) =
        UserModel(
            resultRow[UsersTable.id].value,
            resultRow[UsersTable.firstName],
            resultRow[UsersTable.lastName],
            resultRow[UsersTable.email],
            resultRow[UsersTable.password]
        )

    suspend fun create(user: UserModel): DatabaseStatus {
        try {
            suspendTransaction {
                UsersTable.insert {
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

    suspend fun read(id: Int) = suspendTransaction {
        UsersTable.selectAll()
            .where { UsersTable.id eq id }
            .singleOrNull()
            .let { if(it == null) null
            else resultRowToUserModel(it) }
    }

    suspend fun readByEmail(email: String) = suspendTransaction {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .singleOrNull()
            .let { if(it == null) null
            else resultRowToUserModel(it) }
    }

    suspend fun update(user: UserModel): DatabaseStatus {
        try {
            suspendTransaction {
                UsersTable.update({ UsersTable.id eq user.id }) {
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

    suspend fun updatePassword(user: UserModel): DatabaseStatus {
        try {
            suspendTransaction {
                UsersTable.update({ UsersTable.id eq user.id }) {
                    it[password] = user.password
                }
            }
            return DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            return DatabaseStatus.Incorrect
        }
    }

    suspend fun delete(id: Int) = suspendTransaction {
        UsersTable.deleteWhere { UsersTable.id eq id }
    }
}
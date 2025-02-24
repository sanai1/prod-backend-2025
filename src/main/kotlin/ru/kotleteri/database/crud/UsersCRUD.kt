package ru.kotleteri.database.crud

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kotleteri.data.enums.DatabaseStatus
import ru.kotleteri.data.models.base.UserModel
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

    fun create(user: UserModel): DatabaseStatus {
        try {
            transaction {
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

    fun read(id: Int) = transaction {
        UsersTable.selectAll()
            .where { UsersTable.id eq id }
            .singleOrNull()
            .let { if(it == null) null
            else resultRowToUserModel(it) }
    }

    fun readByEmail(email: String) = transaction {
        UsersTable.selectAll()
            .where { UsersTable.email eq email }
            .singleOrNull()
            .let { if(it == null) null
            else resultRowToUserModel(it) }
    }

    fun update(user: UserModel): DatabaseStatus {
        try {
            transaction {
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

    fun updatePassword(user: UserModel): DatabaseStatus {
        try {
            transaction {
                UsersTable.update({ UsersTable.id eq user.id }) {
                    it[password] = user.password
                }
            }
            return DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            return DatabaseStatus.Incorrect
        }
    }

    fun delete(id: Int) = transaction {
        UsersTable.deleteWhere { UsersTable.id eq id }
    }
}
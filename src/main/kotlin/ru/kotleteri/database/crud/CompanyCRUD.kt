package ru.kotleteri.database.crud

import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import ru.kotleteri.data.enums.DatabaseStatus
import ru.kotleteri.data.models.base.CompanyModel
import ru.kotleteri.database.suspendTransaction
import ru.kotleteri.database.tables.CompanyTable
import java.sql.SQLIntegrityConstraintViolationException
import java.util.*

object CompanyCRUD {
    fun resultRowToCompanyModel(resultRow: ResultRow) =
        CompanyModel(
            id = resultRow[CompanyTable.id].value,
            name = resultRow[CompanyTable.name],
            email = resultRow[CompanyTable.email],
            password = resultRow[CompanyTable.password],
        )


    suspend fun create(company: CompanyModel): DatabaseStatus = suspendTransaction {
        try {
            CompanyTable.insert {
                it[id] = company.id
                it[name] = company.name
                it[email] = company.email
                it[password] = company.password
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
        CompanyTable.selectAll()
            .where { CompanyTable.id eq id }
            .singleOrNull()
            ?.let {
                resultRowToCompanyModel(it)
            }
    }

    suspend fun readByEmail(email: String) = suspendTransaction {
        CompanyTable.selectAll()
            .where { CompanyTable.email eq email }
            .singleOrNull()
            ?.let {
                resultRowToCompanyModel(it)
            }
    }

    suspend fun update(company: CompanyModel): DatabaseStatus = suspendTransaction {
        try {
            CompanyTable.update({ CompanyTable.id eq company.id }) {
                it[name] = company.name
                it[email] = company.email
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

    suspend fun updatePassword(company: CompanyModel): DatabaseStatus = suspendTransaction {
        try {
            CompanyTable.update({ CompanyTable.id eq company.id }) {
                it[password] = company.password
            }
            return@suspendTransaction DatabaseStatus.Correct
        } catch (e: ExposedSQLException) {
            return@suspendTransaction DatabaseStatus.Incorrect
        }
    }

    suspend fun delete(id: UUID) = suspendTransaction {
        CompanyTable.deleteWhere { CompanyTable.id eq id }
    }
}

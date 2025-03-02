package ru.kotleteri.database

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction


val postgresDispatcher = Dispatchers.IO.limitedParallelism(100)

suspend fun <T> suspendTransaction(block: suspend Transaction.() -> T): T =
    newSuspendedTransaction(postgresDispatcher) { block() }
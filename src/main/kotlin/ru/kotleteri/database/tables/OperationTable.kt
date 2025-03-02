package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object OperationTable: IdTable<UUID>("operations") {
    override val id = uuid("id").entityId().uniqueIndex()
    val clientId = uuid("client_id")
    val companyId = uuid("company_id")
    val offerId = uuid("offer_id")
    val timestamp = datetime("timestamp")
    val companyName = varchar("company_name", 50)
    val offerTitle = varchar("offer_title", 50)
    val clientAge = integer("client_age").nullable()
    val clientGender = varchar("client_gender", 6).nullable()


}
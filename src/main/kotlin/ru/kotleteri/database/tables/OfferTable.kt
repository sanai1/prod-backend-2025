package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object OfferTable: IdTable<UUID>("offers") {
    override val id = uuid("id").uniqueIndex().entityId()
    val companyId = reference("company_id", CompanyTable.id)
    val title = varchar("title", 50)
    val description = text("description")
    val discount = double("discount")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
}
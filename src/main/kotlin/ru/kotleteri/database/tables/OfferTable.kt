package ru.kotleteri.database.tables

import org.jetbrains.exposed.dao.id.IdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import ru.kotleteri.data.enums.LoyaltyType
import java.util.*

object OfferTable : IdTable<UUID>("offers") {
    override val id = uuid("id").uniqueIndex().entityId()
    val type = enumerationByName<LoyaltyType>("type", 8)
    val companyId = reference("company_id", CompanyTable.id, onDelete = ReferenceOption.CASCADE)
    val title = varchar("title", 50)
    val description = text("description")
    val startDate = datetime("start_date")
    val endDate = datetime("end_date")
    val discount = double("discount").nullable()
    val bonusFromPurchases = double("bonus_from_purchases").nullable()
    val bonusPaymentPercent = double("bonus_payment_percent").nullable()
}
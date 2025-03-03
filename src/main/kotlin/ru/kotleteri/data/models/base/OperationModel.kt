package ru.kotleteri.data.models.base

import java.time.LocalDateTime
import java.util.*

data class OperationModel(
    val id: UUID,
    val clientId: UUID,
    val companyId: UUID,
    val offerId: UUID,
    val timestamp: LocalDateTime,
    val companyName: String,
    val offerTitle: String,
    val clientAge: Int?,
    val clientGender: String?,
    val cost: Double
)

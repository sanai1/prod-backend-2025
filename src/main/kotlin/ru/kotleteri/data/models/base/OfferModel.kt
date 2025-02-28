package ru.kotleteri.data.models.base

import java.time.LocalDateTime
import java.util.UUID

data class OfferModel(
    val id: UUID,
    val companyId: UUID,
    val title: String,
    val description: String,
    val discount: Double,
    val startDate: LocalDateTime,
    val endDate: LocalDateTime
)
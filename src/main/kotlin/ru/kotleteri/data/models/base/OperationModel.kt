package ru.kotleteri.data.models.base

import java.time.LocalDateTime
import java.util.*

data class OperationModel(
    val id: UUID,
    val clientId: UUID,
    val offerId: UUID,
    val timestamp: LocalDateTime
)

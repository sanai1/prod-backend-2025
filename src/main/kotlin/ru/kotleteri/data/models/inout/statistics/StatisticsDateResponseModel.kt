package ru.kotleteri.data.models.inout.statistics

import kotlinx.serialization.Serializable
import java.time.LocalDate

@Serializable
data class StatisticsDateResponseModel(
    val date: String,
    val operations_count: Int,
    val male_operations_count: Int,
    val female_operations_count: Int
)

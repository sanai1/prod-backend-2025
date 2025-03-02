package ru.kotleteri.data.models.inout.statistics

import kotlinx.serialization.Serializable

@Serializable
data class StatisticsMonthResponseModel(
    val year: Int,
    val month: Int,
    val operations_amount: Int,
    val male_operations_amount: Int,
    val female_operations_amount: Int
)

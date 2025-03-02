package ru.kotleteri.data.models.inout.statistics

import kotlinx.serialization.Serializable

@Serializable
data class StatisticsHourResponseModel(
    val date: String,
    val hour: Int,
    val operations_amount: Int,
    val male_operations_amount: Int,
    val female_operations_amount: Int
)

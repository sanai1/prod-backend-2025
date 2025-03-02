package ru.kotleteri.data.models.inout.statistics

import kotlinx.serialization.Serializable

@Serializable
data class StatisticsDateResponseModel(
    val date: String,
    val operations_amount: Int,
    val male_operations_amount: Int,
    val female_operations_amount: Int
)

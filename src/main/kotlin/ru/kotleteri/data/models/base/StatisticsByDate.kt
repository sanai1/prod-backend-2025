package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.statistics.StatisticsDateResponseModel
import java.time.LocalDate

data class StatisticsByDate(
    val date: LocalDate,
    val operationsCount: Int,
    val maleOperationsCount: Int,
    val femaleOperationsCount: Int
) {
    fun toResponseModel() =
        StatisticsDateResponseModel(
            date.toString(),
            operationsCount,
            maleOperationsCount,
            femaleOperationsCount
        )
}

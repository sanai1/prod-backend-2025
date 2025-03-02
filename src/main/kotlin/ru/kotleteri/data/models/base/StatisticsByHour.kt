package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.statistics.StatisticsHourResponseModel
import java.time.LocalDate

data class StatisticsByHour(
    val date: LocalDate,
    val hour: Int,
    val operationsCount: Int,
    val maleOperationsCount: Int,
    val femaleOperationsCount: Int
) {
    fun toResponseModel() =
        StatisticsHourResponseModel(
            date.toString(),
            hour,
            operationsCount,
            maleOperationsCount,
            femaleOperationsCount
        )
}

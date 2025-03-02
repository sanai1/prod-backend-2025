package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.statistics.StatisticsMonthResponseModel

data class StatisticsByMonth(
    val year: Int,
    val month: Int,
    val operationsCount: Int,
    val maleOperationsCount: Int,
    val femaleOperationsCount: Int
) {
    fun toResponseModel()  =
        StatisticsMonthResponseModel(
            year,
            month,
            operationsCount,
            maleOperationsCount,
            femaleOperationsCount
        )
}

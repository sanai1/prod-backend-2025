package ru.kotleteri.data.models.base

import ru.kotleteri.data.models.inout.statistics.StatisticsMonthResponseModel

data class StatisticsByMonth(
    val year: Int,
    val month: Int,
    val operationsCount: Int,
    val maleOperationsCount: Int,
    val femaleOperationsCount: Int,
    val kidsOperationsCount: Int,
    val youngOperationsCount: Int,
    val middleOperationsCount: Int,
    val oldOperationsCount: Int
) {
    fun toResponseModel() =
        StatisticsMonthResponseModel(
            year,
            month,
            operationsCount,
            maleOperationsCount,
            femaleOperationsCount,
            kidsOperationsCount,
            youngOperationsCount,
            middleOperationsCount,
            oldOperationsCount
        )
}

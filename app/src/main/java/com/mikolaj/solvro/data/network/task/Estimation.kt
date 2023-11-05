package com.mikolaj.solvro.data.network.task

import java.lang.IllegalArgumentException

enum class Estimation {
    ONE, TWO, THREE, FIVE, EIGHT, THIRTEEN, TWENTY_ONE
}

fun Estimation.toNumber(): Int {
    return when (this) {
        Estimation.ONE -> 1
        Estimation.TWO -> 2
        Estimation.THREE -> 3
        Estimation.FIVE -> 5
        Estimation.EIGHT -> 8
        Estimation.THIRTEEN -> 13
        Estimation.TWENTY_ONE -> 21
    }
}

fun Int.fromNumberToEstimation(): Estimation {
    return when (this) {
        1 -> Estimation.ONE
        2 -> Estimation.TWO
        3 -> Estimation.THREE
        5 -> Estimation.FIVE
        8 -> Estimation.EIGHT
        13 -> Estimation.THIRTEEN
        21 -> Estimation.TWENTY_ONE
        else -> throw IllegalArgumentException("Number doesn't belong to Estimation enum class")
    }
}



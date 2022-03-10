package com.lumen.bikeme.repository

object DateNamesRepository {

    private val dateMonthsNames = mapOf(
        1 to "Styczeń",
        2 to "Luty",
        3 to "Marzec",
        4 to "Kwiecień",
        5 to "Maj",
        6 to "Czerwiec",
        7 to "Lipiec",
        8 to "Sierpień",
        9 to "Wrzesień",
        10 to "Październik",
        11 to "Listopad",
        12 to "Grudzień",
    )

    fun getMonthName(month: Int): String {
        return dateMonthsNames[month] ?: "Kosmos️️"
    }
}

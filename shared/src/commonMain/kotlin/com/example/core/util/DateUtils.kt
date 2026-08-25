package com.example.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun getCurrentFormattedDate(): String {
    val current = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
    val monthStr = months[current.monthNumber - 1]
    val dayStr = current.dayOfMonth.toString().padStart(2, '0')
    return "$dayStr $monthStr ${current.year}"
}

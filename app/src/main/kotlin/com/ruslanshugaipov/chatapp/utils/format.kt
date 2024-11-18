package com.ruslanshugaipov.chatapp.utils

import android.content.Context
import com.ruslanshugaipov.chatapp.R
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toJavaInstant
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.floor
import kotlin.math.ln
import kotlin.math.pow

fun formatBytes(bytes: Int, decimals: Int = 2): String {
    if (bytes == 0) return "0 Bytes"

    val k = 1024.0
    val dm = if (decimals < 0) 0 else decimals
    val sizes = listOf("Bytes", "KB", "MB", "GB", "TB", "PB", "EB", "ZB", "YB")
    val i = floor(ln(bytes.toDouble()) / ln(k)).toInt()

    return "%.${dm}f ${sizes[i]}".format(bytes / k.pow(i))
}

fun messageDate(context: Context, date: Instant?): String {
    if (date == null) {
        return ""
    }
    val currentDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val dateLocalTime = date.toLocalDateTime(TimeZone.currentSystemDefault()).date

    //Yesterday
    if ((currentDate - dateLocalTime).days == 1) {
        return context.resources.getString(R.string.yesterday)
    }

    var format = R.string.format_date
    //Current day
    if (currentDate == dateLocalTime) {
        format = R.string.format_current_day
    }

    return formatDate(date, context.resources.getString(format))
}

fun formatDate(date: Instant, format: String): String {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern(format)
        .withZone(ZoneId.systemDefault())

    return formatter.format(date.toJavaInstant())
}

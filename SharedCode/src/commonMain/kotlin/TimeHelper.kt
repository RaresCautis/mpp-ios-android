package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import kotlin.math.abs
import kotlin.math.floor

class TimeHelper {
    companion object {
        fun getJourneyTime(timeInMinutes: Int): String {
            val hours = timeInMinutes / 60
            val minutes = "${timeInMinutes % 60}".padStart(2, '0')

            return "$hours:$minutes"
        }

        fun formatDateTimeOutput(dateTime: String): DateTimeStrings {
            val journeyDateFormat = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val utcDateTime = journeyDateFormat.parse(dateTime).utc.local

            val date = utcDateTime.format("yyyy-MM-dd")
            val time = utcDateTime.format("HH:mm")

            return DateTimeStrings(date, time)
        }

        fun formatDateTimeInput(input: String, format: String): String {
            val dateTime = DateFormat(format).parse(input).utc.local

            return dateTime.format("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        }
    }
}
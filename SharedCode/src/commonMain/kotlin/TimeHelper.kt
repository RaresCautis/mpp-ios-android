package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import kotlin.math.abs
import kotlin.math.floor

class TimeHelper {
    companion object {
        fun getJourneyTime(departureTime: String, arrivalTime: String): String {
            val dateFormat = DateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val departureDate = dateFormat.parse(departureTime)
            val arrivalDate = dateFormat.parse(arrivalTime)

            val journeyTimeHours = floor((arrivalDate - departureDate).hours).toInt()
            val journeyTimeMinutes = floor((arrivalDate - departureDate).minutes % 60).toInt()

            if(journeyTimeHours < 0 || journeyTimeMinutes < 0)
                return "INVALID JOURNEY TIME"

            return addZeroToDateTime(journeyTimeHours) + ":" + addZeroToDateTime(journeyTimeMinutes)
        }

        private fun addZeroToDateTime(value: Int): String {
            return if (abs(value) < 10) {
                "0$value"
            } else {
                "$value"
            }
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
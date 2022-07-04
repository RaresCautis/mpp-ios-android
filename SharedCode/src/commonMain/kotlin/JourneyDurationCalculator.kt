package com.jetbrains.handson.mpp.mobile

import com.soywiz.klock.DateFormat
import com.soywiz.klock.parse
import kotlin.math.floor

class JourneyDurationCalculator {
    companion object {
        fun getJourneyTime(departureTime: String, arrivalTime: String) : String {
            val dateFormat: DateFormat = DateFormat("yyyy-MM-ddTHH:mm")

            val departureDate = dateFormat.parse("${departureTime.subSequence(0,16)}")
            val arrivalDate = dateFormat.parse("${arrivalTime.subSequence(0,16)}")

            val journeyTimeHours = floor((arrivalDate - departureDate).hours).toInt()
            val journeyTimeMinutes= floor((arrivalDate - departureDate).minutes % 60).toInt()

            return addZeroToDateTime(journeyTimeHours) + ":" + addZeroToDateTime(journeyTimeMinutes)
        }

        private fun addZeroToDateTime(value: Int) : String {
            return if (value < 10) {
                "0$value"
            } else {"$value"}
        }
    }
}
package com.jetbrains.handson.mpp.mobile

import org.junit.Assert
import org.junit.Test

class TimeHelperTest {


    @Test
    fun getJourneyTime_isCorrect() {
        val departure_time = "2022-07-24T14:34:00.000+01:00"
        val arrival_time = "2022-07-24T15:35:00.000+01:00"

        val journey_time = TimeHelper.getJourneyTime(departure_time, arrival_time)

        Assert.assertEquals("01:01", journey_time)
    }

    @Test
    fun getJourneyTime_longTimeJourneyIsCorrect() {
        val departure_time = "2022-07-24T14:34:00.000+01:00"
        val arrival_time = "2022-07-26T15:35:00.000+01:00"

        val journey_time = TimeHelper.getJourneyTime(departure_time, arrival_time)

        Assert.assertEquals("49:01", journey_time)
    }

    @Test
    fun getJourneyTime_invalidInput() {
        val departure_time = "2022-07-24T14:54:00.000+01:00"
        val arrival_time = "2022-07-24T13:35:00.000+01:00"

        val journey_time = TimeHelper.getJourneyTime(departure_time, arrival_time)

        Assert.assertEquals("INVALID JOURNEY TIME", journey_time)
    }

    @Test
    fun getJourneyTime_noJourneyTime() {
        val departure_time = "2022-07-24T14:54:00.000+01:00"
        val arrival_time = "2022-07-24T14:54:00.000+01:00"

        val journey_time = TimeHelper.getJourneyTime(departure_time, arrival_time)

        Assert.assertEquals("00:00", journey_time)
    }

    @Test
    fun getJourneyTime_differentTimeZones() {
        val departure_time = "2022-07-24T14:54:00.000+01:00"
        val arrival_time = "2022-07-25T12:54:00.000+00:00"

        val journey_time = TimeHelper.getJourneyTime(departure_time, arrival_time)

        Assert.assertEquals("23:00", journey_time)
    }

    @Test
    fun formatDateTimeOutput_isCorrect() {
        val dateTime = "2022-07-24T14:54:00.000+01:00"

        val dateTimeStrings = TimeHelper.formatDateTimeOutput(dateTime)

        Assert.assertEquals("2022-07-24", dateTimeStrings.date)
        Assert.assertEquals("14:54", dateTimeStrings.time)
    }

    @Test
    fun formatDateTimeOutput_differentTimezone() {
        val dateTimeUtc = "2022-07-24T14:54:00.000+00:00"

        val dateTimeStringsUtc = TimeHelper.formatDateTimeOutput(dateTimeUtc)

        Assert.assertEquals("2022-07-24", dateTimeStringsUtc.date)
        Assert.assertEquals("15:54", dateTimeStringsUtc.time)

        val dateTimeRandom = "2022-07-24T14:54:00.000+05:00"

        val dateTimeStringsRandom = TimeHelper.formatDateTimeOutput(dateTimeRandom)

        Assert.assertEquals("2022-07-24", dateTimeStringsRandom.date)
        Assert.assertEquals("10:54", dateTimeStringsRandom.time)
    }

    @Test
    fun formatDateTimeInput_isCorrectIos() {
        val dateTime = "2022-07-24 14:54:00 +0000"
        val format = "yyyy-MM-dd HH:mm:ss z"

        val dateTimeReturned = TimeHelper.formatDateTimeInput(dateTime, format)

        Assert.assertEquals("2022-07-24T15:54:00.000+01:00", dateTimeReturned)
    }

    @Test
    fun formatDateTimeInput_isCorrectAndroid() {
        val dateTime = "Sun Jul 24 14:54:00 UTC+00:00 2022"
        val format = "EEE MMM d HH:mm:ss z yyyy"

        val dateTimeReturned = TimeHelper.formatDateTimeInput(dateTime, format)

        Assert.assertEquals("2022-07-24T15:54:00.000+01:00", dateTimeReturned)
    }

    @Test
    fun formatDateTimeInput_differentDayTimezone() {
        val dateTime = "Sun Jul 24 23:54:00 UTC+00:00 2022"
        val format = "EEE MMM d HH:mm:ss z yyyy"

        val dateTimeReturned = TimeHelper.formatDateTimeInput(dateTime, format)

        Assert.assertEquals("2022-07-25T00:54:00.000+01:00", dateTimeReturned)
    }
}
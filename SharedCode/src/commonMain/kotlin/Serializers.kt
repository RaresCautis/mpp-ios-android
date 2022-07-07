package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val error: String, val error_description: String)

@Serializable
data class DepartureDetails (
    val outboundJourneys : List<JourneyDetails>
    )

@Serializable
data class JourneyDetails (
    val departureTime: String,
    val arrivalTime: String,
    val primaryTrainOperator: TrainOperatorDetails,
    val tickets: List<TicketDetails>
    )

@Serializable
data class TrainOperatorDetails (
    val name: String
    )

@Serializable
data class TicketDetails (
    val priceInPennies: Int
    )

data class DepartureInformation (
    val departureDateTime: DateTimeStrings,
    val arrivalDateTime: DateTimeStrings,
    val journeyTime: String,
    val price: String
    )

data class DateTimeStrings (
    val date: String,
    val time: String
    )

@Serializable
data class Stations (
    val stations: List<StationDetails>
        )

@Serializable
data class StationDetails (
    val name: String,
    val crs: String ?
        )
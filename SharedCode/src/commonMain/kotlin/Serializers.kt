package com.jetbrains.handson.mpp.mobile

import kotlinx.serialization.Serializable

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
    val departureDateTime: DateTime,
    val arrivalDateTime: DateTime,
    val journeyTime: String,
    val price: String
    )

data class DateTime (
    val date: String,
    val time: String
    )
package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration

class ApplicationPresenter : ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val baseURL = "https://mobile-api-softwire2.lner.co.uk/"
    private val client = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job


    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        launch {
           view.setStationNames(StationHelper.getStations())
        }
    }

    private suspend fun getAPITimeData(
        originCrs: String,
        destinationCrs: String,
        dateTime: String,
        adultCount: String,
        childCount: String
    ): DepartureDetails {
        val url = URLBuilder("${baseURL}v1/fares?").apply {
            parameters["originStation"] = originCrs
            parameters["destinationStation"] = destinationCrs
            parameters["outboundDateTime"] = dateTime
            parameters["numberOfChildren"] = childCount
            parameters["numberOfAdults"] = adultCount
        }.build()

        return client.get {
            url(url)
        }
    }

    override fun makeTrainSearch(
        originCrs: String,
        destinationCrs: String,
        dateTime: String,
        adultCount: String,
        childCount: String
    ) {
        launch {
            try {
                val departureDetails =
                    getAPITimeData(originCrs, destinationCrs, dateTime, adultCount, childCount)

                val data = departureDetails.outboundJourneys.map {
                    DepartureInformation(
                        departureDateTime = TimeHelper.formatDateTimeOutput(it.departureTime),
                        arrivalDateTime = TimeHelper.formatDateTimeOutput(it.arrivalTime),
                        journeyTime = TimeHelper.getJourneyTime(it.departureTime, it.arrivalTime),
                        price = pricePenniesToPounds(it.tickets.first().priceInPennies)
                    )
                }

                if (data.isEmpty()) view?.createAlert("No valid journey.", "ERROR")


                view?.setTableData(data)
            } catch (e: ClientRequestException) {
                val responseText = e.response.readText()
                val json = Json(JsonConfiguration.Stable)
                val description = json.parse(ErrorResponse.serializer(), responseText)
                view?.createAlert(description.error_description, "ERROR")
            }
        }
    }

    override fun formatDateTimeInput(input: String, format: String) =
        TimeHelper.formatDateTimeInput(input, format)

    private fun pricePenniesToPounds(price: Int): String {
        val pounds = price / 100
        val pennies = "${price % 100}".padStart(2, '0')

        return "£${pounds}.${pennies}"
    }
}

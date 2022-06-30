package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import kotlinx.serialization.json.Json
import kotlin.math.min

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val stations = listOf("KGX","EDB", "YRK", "DHM", "NCL")
    private val baseURL = "https://mobile-api-softwire2.lner.co.uk/"
    private val client = HttpClient{
        install(JsonFeature){
            serializer = KotlinxSerializer(Json.nonstrict)
        }
    }

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setStationNames(stations)
    }

    private suspend fun getAPITimeData(originCrs: String, destinationCrs: String, dateTime: String): DepartureDetails {
        val url = URLBuilder("${baseURL}v1/fares?").apply{
            parameters["originStation"] = originCrs
            parameters["destinationStation"] = destinationCrs
            parameters["outboundDateTime"] = dateTime
            parameters["numberOfChildren"] = "0"
            parameters["numberOfAdults"] = "2"
        }.build()

        return client.get{
            url(url)
        }
    }

    private fun formatDateTimeOutput(dateTime: String) : DateTime {
        val date = dateTime.subSequence(0,10).toString()
        val time = dateTime.subSequence(11,16).toString()
        return DateTime(date, time)
    }

    override fun makeTrainSearch(originCrs: String, destinationCrs: String, dateTime: String) {
        launch{
            try{
                val departureDetails = getAPITimeData(originCrs, destinationCrs, dateTime)

                val data = departureDetails.outboundJourneys.map {
                    DepartureInformation(
                        departureDateTime = formatDateTimeOutput(it.departureTime),
                        arrivalDateTime = formatDateTimeOutput(it.arrivalTime)
                    )
                }

                view?.setTableData(data)
            } catch (e: Exception) {
                view?.createAlert("ERROR: Couldn't receive train data.", "Error")
            }
        }

    }
}

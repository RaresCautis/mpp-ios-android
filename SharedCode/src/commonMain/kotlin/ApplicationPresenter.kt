package com.jetbrains.handson.mpp.mobile

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.*
import kotlinx.serialization.serializer
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter: ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()
    private val stations = listOf("KGX","EDB", "YRK", "DHM", "NCL")

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        view.setStationNames(stations)
    }
    private val client = HttpClient{
        install(JsonFeature){
            serializer = KotlinxSerializer(kotlinx.serialization.json.Json.nonstrict)
        }
    }
    private val baseURL = "https://mobile-api-softwire2.lner.co.uk/"

    override fun makeTrainSearch(origin: Int, destination: Int) {
        launch{
            try{
                val url = URLBuilder("${baseURL}v1/fares?").apply{
                    parameters["originStation"] = stations[origin]
                    parameters["destinationStation"] = stations[destination]
                    parameters["outboundDateTime"] = "2022-07-24T14:30:00.000+01:00"
                    parameters["numberOfChildren"] = "0"
                    parameters["numberOfAdults"] = "2"
                }.build()
                val departureDetails = client.get<DepartureDetails> {
                    url(url)
                }

                print("GOT HERE")

                view?.setArrivalTimeLabel(departureDetails.outboundJourneys.first().arrivalTime)
            } catch (e: Exception) {
                print("ERROR")
            }
        }

    }
}

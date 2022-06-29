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



    override fun makeTrainSearch(originCrs: String, destinationCrs: String) {
        launch{
            try{
                val url = URLBuilder("${baseURL}v1/fares?").apply{
                    parameters["originStation"] = originCrs
                    parameters["destinationStation"] = destinationCrs
                    parameters["outboundDateTime"] = "2022-07-24T14:30:00.000+01:00"
                    parameters["numberOfChildren"] = "0"
                    parameters["numberOfAdults"] = "2"
                }.build()
                val departureDetails = client.get<DepartureDetails> {
                    url(url)
                }

                val data: MutableList<DepartureInformation> = mutableListOf<DepartureInformation>()


                for(i in 0 until min(a=5, b=departureDetails.outboundJourneys.size)){
                    val journey = departureDetails.outboundJourneys[i]

                    val departure = DepartureInformation(
                        departureTime = journey.departureTime,
                        arrivalTime = journey.arrivalTime
                    )

                    data.add(departure)
                }

                view?.setTableData(data)
            } catch (e: Exception) {
                view?.createAlert("ERROR: Couldn't receive train data.")
            }
        }

    }
}

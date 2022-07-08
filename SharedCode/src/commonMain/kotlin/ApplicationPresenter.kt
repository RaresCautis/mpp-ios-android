package com.jetbrains.handson.mpp.mobile

import io.ktor.client.features.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ApplicationPresenter : ApplicationContract.Presenter() {

    private val dispatchers = AppDispatchersImpl()
    private var view: ApplicationContract.View? = null
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job


    override fun onViewTaken(view: ApplicationContract.View) {
        this.view = view
        setUpStations()
    }

    private fun setUpStations() {
        launch {
            view?.setStationNames(APIHelper.getStations())
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
                    APIHelper.getAPIData(
                        originCrs,
                        destinationCrs,
                        dateTime,
                        adultCount,
                        childCount
                    )
                val data = departureDetails.outboundJourneys.map {
                    DepartureInformation(
                        departureDateTime = TimeHelper.formatDateTimeOutput(it.departureTime),
                        arrivalDateTime = TimeHelper.formatDateTimeOutput(it.arrivalTime),
                        journeyTime = TimeHelper.getJourneyTime(it.journeyDurationInMinutes),
                        price = penniesToPounds(it.tickets.first().priceInPennies)
                    )
                }

                if (data.isEmpty()) view?.createAlert("ERROR", "No valid journey.")

                view?.setTableData(data)
            } catch (e: ResponseException) {
                view?.createAlert("ERROR", APIHelper.getAPIError(e))
            } catch (e: Exception) {
                view?.createAlert("Error", "An error occurred")
            }

        }
    }

    override fun formatDateTimeInput(input: String, format: String) =
        TimeHelper.formatDateTimeInput(input, format)

    fun penniesToPounds(price: Int): String {
        val pounds = price / 100
        val pennies = "${price % 100}".padStart(2, '0')

        return "£${pounds}.${pennies}"
    }

}


package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStationNames(stationNames: List<String>)
        fun createAlert(alertMessage: String)
        fun setTableData(data: List<DepartureInformation>)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun makeTrainSearch(originCrs: String, destinationCrs: String)
    }
}

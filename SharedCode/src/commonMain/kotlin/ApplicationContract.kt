package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface ApplicationContract {
    interface View {
        fun setStationNames(stationNames: List<String>)
        fun setArrivalTimeLabel(text: String)
    }

    abstract class Presenter: CoroutineScope {
        abstract fun onViewTaken(view: View)
        abstract fun makeTrainSearch(origin: Int, destination: Int)
    }
}

package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.CoroutineScope

interface StationSearchContract {
    interface View {
    }
    abstract class Presenter : CoroutineScope {
        abstract fun onViewTaken(view: View)
    }
}
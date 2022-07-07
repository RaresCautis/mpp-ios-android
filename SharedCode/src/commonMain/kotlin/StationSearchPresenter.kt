package com.jetbrains.handson.mpp.mobile

import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class StationSearchPresenter : StationSearchContract.Presenter() {
    private var view: StationSearchContract.View? = null
    private val dispatchers = AppDispatchersImpl()
    private val job: Job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = dispatchers.main + job

    override fun onViewTaken(view: StationSearchContract.View) {
        this.view = view
    }
}
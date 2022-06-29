package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val presenter = ApplicationPresenter()
        presenter.onViewTaken(this)
    }

    override fun setStationNames(stationNames: List<String>) {
        //To DO
    }

    override fun createAlert(alertMessage: String) {
        //TO DO
    }

    override fun setTableData(data: MutableList<DepartureInformation>) {
        //TO DO
    }

}

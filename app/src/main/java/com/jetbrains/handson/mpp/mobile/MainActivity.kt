package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private var stations: List<String> = listOf()
    private lateinit var presenter: ApplicationContract.Presenter
    private var tableData: List<DepartureInformation> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        setUpSpinners()
    }

    override fun setStationNames(stationNames: List<String>) {
        stations = stationNames
    }

    override fun createAlert(alertMessage: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("ERROR: Couldn't receive train data.")
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay") {
                dialog, _ -> dialog.dismiss()
        }

        alertDialog.show()
    }

    override fun setTableData(data: List<DepartureInformation>) {
        tableData = data
        refreshTableData()
    }

    private fun setUpSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this,android.R.layout.simple_spinner_item, stations
        )
        findViewById<Spinner>(R.id.departureSpinner).adapter = adapter
        findViewById<Spinner>(R.id.arrivalSpinner).adapter = adapter
    }

    fun onButtonTapped(view: View) {
        val originStation = findViewById<Spinner>(R.id.departureSpinner).selectedItem.toString()
        val finalStation = findViewById<Spinner>(R.id.arrivalSpinner).selectedItem.toString()
        presenter.makeTrainSearch(originCrs = originStation, destinationCrs = finalStation)
    }

    private fun refreshTableData() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val data = tableData.map {
            RecyclerViewCell(departureTime = it.departureTime, arrivalTime = it.arrivalTime)
        }
        val adapter = RecyclerViewAdapter(data)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

}

package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_row.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private var stations: List<String> = listOf()
    private lateinit var presenter: ApplicationContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        setUpSpinners()
        setUpSubmitButton()
    }

    override fun setStationNames(stationNames: List<String>) {
        stations = stationNames
    }

    override fun createAlert(alertMessage: String, alertTitle: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(alertTitle)
        builder.setMessage(alertMessage)
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay") {
                dialog, _ -> dialog.dismiss()
        }

        alertDialog.show()
    }

    override fun setTableData(data: List<DepartureInformation>) {
        val tableData = data.map {
            RecyclerViewCell(it.departureDateTime.time,
                it.departureDateTime.date,
                it.arrivalDateTime.time,
                it.arrivalDateTime.date)
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(baseContext,layoutManager.orientation))
        recyclerView.adapter = DepartureBoardRecyclerViewAdapter(tableData)
    }

    private fun setUpSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, stations
        )
        findViewById<Spinner>(R.id.departureSpinner).adapter = adapter
        findViewById<Spinner>(R.id.arrivalSpinner).adapter = adapter
    }

    private fun setUpSubmitButton() {
        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener { submitButtonTapped() }
    }

    private fun submitButtonTapped() {
        val originStation = findViewById<Spinner>(R.id.departureSpinner).selectedItem.toString()
        val finalStation = findViewById<Spinner>(R.id.arrivalSpinner).selectedItem.toString()
        presenter.makeTrainSearch(originStation, finalStation)
    }
}

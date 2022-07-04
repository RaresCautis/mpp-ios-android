package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
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
        setUpTimePicker()
        supportActionBar?.setTitle("Train\uD83D\uDE82")
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
                it.arrivalDateTime.date,
                it.price,
                it.journeyTime)
        }

        val layoutManager = LinearLayoutManager(this)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = DepartureBoardRecyclerViewAdapter(tableData)
    }

    private fun setUpSpinners() {
        val adapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, stations
        )
        departureSpinner.adapter = adapter
        arrivalSpinner.adapter = adapter
    }

    private fun setUpSubmitButton() {
        val submitButton = submitButton
        submitButton.setOnClickListener { submitButtonTapped() }
    }

    private fun submitButtonTapped() {
        val originStation = departureSpinner.selectedItem.toString()
        val finalStation = arrivalSpinner.selectedItem.toString()
        val dateTime = getSelectedDate()
        presenter.makeTrainSearch(originStation, finalStation, dateTime)


    }
    private fun getSelectedDate() : String {
        val day = addZeroToDateTime(datePicker.dayOfMonth)
        val month = addZeroToDateTime(datePicker.month + 1)
        val year = "${datePicker.year}"

        val hour = addZeroToDateTime(timePicker.hour)
        val minute = addZeroToDateTime(timePicker.minute)

        return year + "-" + month + "-" + day + "T" + hour + ":" + minute + ":00.000+01:00"
    }

    private fun setUpTimePicker() {
        timePicker.setIs24HourView(true)
        timePicker.currentHour = timePicker.currentHour + 1
    }

    private fun addZeroToDateTime(value: Int) : String {
        return if (value < 10) {
            "0$value"
        } else {"$value"}
    }





}

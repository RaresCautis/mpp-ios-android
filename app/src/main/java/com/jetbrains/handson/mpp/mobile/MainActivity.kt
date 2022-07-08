package com.jetbrains.handson.mpp.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recyclerview_row.*
import java.lang.Integer.max
import java.lang.Integer.min
import java.util.*

class MainActivity : AppCompatActivity(), ApplicationContract.View {

    private var stations: List<String> = listOf()
    private lateinit var presenter: ApplicationContract.Presenter

    private val buttonToLabel = mapOf(R.id.adultDec to R.id.adultCounter, R.id.adultInc to R.id.adultCounter, R.id.childDec to R.id.childCounter, R.id.childInc to R.id.childCounter)
    private val buttonToModifier = mapOf(R.id.adultDec to -1, R.id.adultInc to 1, R.id.childDec to -1, R.id.childInc to 1)

    private val minNumberTickets = 0
    private val maxNumberTickets = 8

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        presenter = ApplicationPresenter()
        presenter.onViewTaken(this)

        setUpSubmitButton()
        setUpTimePicker()
        supportActionBar?.setTitle("Train\uD83D\uDE82")
    }

    override fun setStationNames(stationNames: List<StationDetails>) {
        stations = stationNames.map { it.crs!! }
        setUpSpinners()
    }

    override fun createAlert(alertTitle: String, alertMessage: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(alertTitle)
        builder.setMessage(alertMessage)
        val alertDialog: AlertDialog = builder.create()

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Okay") { dialog, _ ->
            dialog.dismiss()
        }

        alertDialog.show()
    }

    override fun setTableData(data: List<DepartureInformation>) {
        val tableData = data.map {
            RecyclerViewCell(
                it.departureDateTime.time,
                it.departureDateTime.date,
                it.arrivalDateTime.time,
                it.arrivalDateTime.date,
                it.price,
                it.journeyTime
            )
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
        val originStation = "${departureSpinner.selectedItem}"
        val finalStation = "${arrivalSpinner.selectedItem}"
        val dateTime = getSelectedDate()
        val adultCount = "${adultCounter.text}"
        val childCount = "${childCounter.text}"
        presenter.makeTrainSearch(originStation, finalStation, dateTime, adultCount, childCount)
    }

    fun counterButtonTapped(view: View) {
        val buttonId = view.id
        val label = findViewById<TextView>(buttonToLabel[buttonId]!!)
        val labelValue = "${label.text}".toInt()

        val newLabelValue = boundInt(labelValue + buttonToModifier[buttonId]!!, minNumberTickets, maxNumberTickets)

        label.text = newLabelValue.toString()
    }

    private fun boundInt(input: Int, minVal: Int, maxVal: Int) = max(min(input, maxVal), minVal)

    private fun getSelectedDate(): String {
        val calendar = Calendar.getInstance()
        calendar.set(
            datePicker.year,
            datePicker.month,
            datePicker.dayOfMonth,
            timePicker.hour,
            timePicker.minute
        )

        return presenter.formatDateTimeInput(calendar.time.toString(), "EEE MMM d HH:mm:ss z yyyy")
    }

    private fun setUpTimePicker() {
        timePicker.setIs24HourView(true)
        timePicker.currentHour = timePicker.currentHour + 1
    }

}

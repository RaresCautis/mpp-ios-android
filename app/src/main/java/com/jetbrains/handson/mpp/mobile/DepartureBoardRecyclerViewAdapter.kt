package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.recyclerview_row.view.*

class DepartureBoardRecyclerViewAdapter(private val tableDataCells: List<RecyclerViewCell>) :
    RecyclerView.Adapter<DepartureBoardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val recyclerCell = tableDataCells[position]

        holder.departureTime.text = recyclerCell.departureTime
        holder.departureDate.text = recyclerCell.departureDate
        holder.arrivalTime.text = recyclerCell.arrivalTime
        holder.arrivalDate.text = recyclerCell.arrivalDate
        holder.price.text = recyclerCell.price
        holder.journeyTime.text = recyclerCell.journeyTime
    }

    override fun getItemCount(): Int {
        return tableDataCells.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val departureTime: TextView = itemView.departureTime
        val departureDate: TextView = itemView.departureDate
        val arrivalTime: TextView = itemView.arrivalTime
        val arrivalDate: TextView = itemView.arrivalDate
        val price: TextView = itemView.price
        val journeyTime: TextView = itemView.journeyTime
    }
}

data class RecyclerViewCell(
    val departureTime: String,
    val departureDate: String,
    val arrivalTime: String,
    val arrivalDate: String,
    val price: String,
    val journeyTime: String
) {
}
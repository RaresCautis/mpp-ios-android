package com.jetbrains.handson.mpp.mobile

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class DepartureBoardRecyclerViewAdapter(private val tableDataCells: List<RecyclerViewCell>) : RecyclerView.Adapter<DepartureBoardRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_row, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemsViewModel = tableDataCells[position]

        holder.departureTime.text = itemsViewModel.departureTime
        holder.departureDate.text = itemsViewModel.departureDate
        holder.arrivalTime.text = itemsViewModel.arrivalTime
        holder.arrivalDate.text = itemsViewModel.arrivalDate
    }

    override fun getItemCount(): Int {
        return tableDataCells.size
    }

    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val departureTime: TextView = itemView.findViewById(R.id.departureTime)
        val departureDate: TextView = itemView.findViewById(R.id.departureDate)
        val arrivalTime: TextView = itemView.findViewById(R.id.arrivalTime)
        val arrivalDate: TextView = itemView.findViewById(R.id.arrivalDate)
    }
}

data class RecyclerViewCell(val departureTime: String, val departureDate: String, val arrivalTime: String, val arrivalDate: String) {
}
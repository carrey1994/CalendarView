package com.example.customcalendarview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CalendarAdapter(private val context: Context) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CalendarViewHolder {
        return CalendarViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_calendar,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 12

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.customCalendarView.setDateRecyclerView(position)
    }

    inner class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val customCalendarView: CustomCalendarView by lazy {
            view.findViewById<CustomCalendarView>(R.id.custom_calendar_view)
        }
    }
}
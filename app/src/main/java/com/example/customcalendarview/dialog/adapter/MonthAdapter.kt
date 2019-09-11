package com.example.customcalendarview.dialog.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendarview.R
import java.text.DateFormatSymbols
import java.util.*

class MonthAdapter(val context: Context) : RecyclerView.Adapter<MonthAdapter.MonthViewHolder>() {

    private lateinit var selectMonthListener: SelectMonthListener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MonthViewHolder {
        return MonthViewHolder(View.inflate(context, R.layout.item_month, null))
    }

    override fun getItemCount(): Int = 12

    override fun onBindViewHolder(holder: MonthViewHolder, position: Int) {
        holder.tvMonth
        holder.tvMonth.apply {
            text = DateFormatSymbols().months[position]
            setOnClickListener {
                selectMonthListener.onSelectMonth(position)
            }
        }
    }

    inner class MonthViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvMonth = view.findViewById<TextView>(R.id.tv_month)
    }

    interface SelectMonthListener {
        fun onSelectMonth(month: Int)
    }

    fun setSelectMonthListener(listener: SelectMonthListener) {
        this.selectMonthListener = listener
    }

}
package com.example.customcalendarview.dialog.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendarview.R
import java.util.*

class YearAdapter(val context: Context) : RecyclerView.Adapter<YearAdapter.YearViewHolder>() {

    private lateinit var selectYearListener: SelectYearListener

    private val years = mutableListOf<Int>().apply {
        for (year in 1950..Calendar.getInstance().get(Calendar.YEAR)) {
            add(year)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YearViewHolder {
        val view = View.inflate(context, R.layout.item_year, null)
        return YearViewHolder(view)
    }

    override fun getItemCount(): Int = years.size

    override fun onBindViewHolder(holder: YearViewHolder, position: Int) {
        holder.tvYear.text = "${years[position]}"
        holder.tvYear.setOnClickListener {
            selectYearListener.onSelectYear(years[position])
        }
    }

    inner class YearViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvYear
                = view.findViewById<TextView>(R.id.tv_year)
    }

    interface SelectYearListener {
        fun onSelectYear(year: Int)
    }

    fun setSelectYearListener(listener: SelectYearListener) {
        this.selectYearListener = listener
    }
}
package com.example.customcalendarview

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import java.util.*

class CalendarPageAdapter(
    private val context: Context,
    private val year: Int = Calendar.getInstance().get(Calendar.YEAR)
) : PagerAdapter() {

    private lateinit var datePicker: DatePickerListener

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view === `object`

    override fun getCount(): Int = 12

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.item_calendar, container, false)
        val calendarView = view.findViewById<CustomCalendarView>(R.id.custom_calendar_view)
        calendarView.setDateRecyclerView(position, year)
        calendarView.setDateListener(object : DateAdapter.DateListener {
            override fun onDate(year: Int, month: Int, date: Int) {
                datePicker.onDatePicker(year, month, date)
            }
        })
        calendarView.tag = position
        container.addView(view)
        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }

    interface DatePickerListener {
        fun onDatePicker(year: Int, month: Int, date: Int)
    }

    fun setDatePickerListener(datePicker: DatePickerListener) {
        this.datePicker = datePicker
    }

}
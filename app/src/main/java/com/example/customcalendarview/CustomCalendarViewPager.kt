package com.example.customcalendarview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import java.util.*

class CustomCalendarViewPager : LinearLayout {

    private val datePickList = mutableListOf<String>()

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val layoutView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.layout_calendar_viewpager, null, false)
    }

    private val vpCalendar by lazy {
        layoutView.findViewById<ViewPager>(R.id.vp_calendar)
    }

    init {
        val calendarPageAdapter = CalendarPageAdapter(context)
        vpCalendar.offscreenPageLimit = 12
        addDatePickerListener(calendarPageAdapter)
        vpCalendar.currentItem = Calendar.getInstance().get(Calendar.MONTH)
        this.addView(layoutView)
    }

    private fun addDatePickerListener(calendarPageAdapter: CalendarPageAdapter) {
        calendarPageAdapter.setDatePickerListener(object : CalendarPageAdapter.DatePickerListener {
            override fun onDatePicker(year: Int, month: Int, date: Int) {
                val pickDate = "$year - $month - $date"
                if (datePickList.contains(pickDate).not())
                    datePickList.add(pickDate)
                else
                    datePickList.remove(pickDate)
                Log.e("PickDate===", "$datePickList===")
            }
        })
        calendarPageAdapter.setYearChangeListener(object : CalendarPageAdapter.YearChangeListener {
            override fun onYearChangeListener(year: Int) {
                datePickList.clear()
                val changeYearCalendarAdapter = CalendarPageAdapter(context, year)
                addDatePickerListener(changeYearCalendarAdapter)
            }
        })
        calendarPageAdapter.setMonthChangeListener(object :
            CalendarPageAdapter.MonthChangeListener {
            override fun onMonthChangListener(month: Int) {
                vpCalendar.currentItem = month
            }
        })
        vpCalendar.adapter = calendarPageAdapter
    }

}


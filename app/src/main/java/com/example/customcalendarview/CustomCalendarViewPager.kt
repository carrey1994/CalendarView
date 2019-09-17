package com.example.customcalendarview

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.layout_calendar.view.*
import java.lang.Exception
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

    private val tvMonth by lazy {
        layoutView.findViewById<TextView>(R.id.tv_month)
    }

    private val tvYear by lazy {
        layoutView.findViewById<TextView>(R.id.tv_year)
    }

    private val tvClear by lazy {
        layoutView.findViewById<TextView>(R.id.tv_clear)
    }

    init {
        val calendarPageAdapter = CalendarPageAdapter(context)
        vpCalendar.offscreenPageLimit = 12
        addDatePickerListener(calendarPageAdapter)
        vpCalendar.currentItem = Calendar.getInstance().get(Calendar.MONTH)
        vpCalendar.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                setMonthText(position)
            }
        })

        tvYear.text = "${Calendar.getInstance().get(Calendar.YEAR)}"
        tvMonth.text = Calendar.getInstance()
            .getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        tvYear.setOnClickListener {
            val yearDialog = YearDialog()
            yearDialog.show((context as FragmentActivity).supportFragmentManager, null)
            yearDialog.setOnYearPickerListener(object : YearDialog.OnYearPickerListener {
                override fun onYear(year: Int) {
                    tvYear.text = "$year"
                    setMonthText(0)
                    datePickList.clear()
                    val changeYearCalendarAdapter = CalendarPageAdapter(context, year)
                    addDatePickerListener(changeYearCalendarAdapter)
                }
            })
        }

        tvMonth.setOnClickListener {
            val monthDialog = MonthDialog()
            monthDialog.show((context as FragmentActivity).supportFragmentManager, null)
            monthDialog.setOnMonthPickerListener(object : MonthDialog.OnMonthPickerListener {
                override fun onMonth(month: Int) {
                    vpCalendar.currentItem = month
                    setMonthText(month)
                }
            })
        }

        tvClear.setOnClickListener {
            CustomCalendarManager.pickDates.clear()
            notifyDataOverMonth()
            CustomCalendarManager.notifyPositions.clear()
        }

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
        vpCalendar.adapter = calendarPageAdapter
        CustomCalendarManager.viewPager = vpCalendar
    }

    private fun notifyDataOverMonth() {
        CustomCalendarManager.notifyPositions.sort()
        if (CustomCalendarManager.notifyPositions.size == 1) {
            val month = CustomCalendarManager.notifyPositions[0]
            vpCalendar.findViewWithTag<CustomCalendarView>(month).rv_calendar.adapter!!.notifyDataSetChanged()
        } else if (CustomCalendarManager.notifyPositions.size == 2) {
            val startMonth = CustomCalendarManager.notifyPositions[0]
            val endMonth = CustomCalendarManager.notifyPositions[1]
            for (month in startMonth..endMonth) {
                vpCalendar.findViewWithTag<CustomCalendarView>(month).rv_calendar.adapter!!.notifyDataSetChanged()
            }
        }
    }

    private fun setMonthText(month: Int) {
        tvMonth.text = Calendar.getInstance().apply { set(Calendar.MONTH, month) }
            .getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    }

}


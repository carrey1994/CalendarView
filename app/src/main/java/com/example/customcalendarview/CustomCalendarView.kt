package com.example.customcalendarview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.layout_calendar.view.*


class CustomCalendarView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    data class CustomDateModel(var year: Int, var month: Int, var date: Int)

    private val mDateModel = CustomDateModel(
        Calendar.getInstance().get(Calendar.YEAR),
        Calendar.getInstance().get(Calendar.MONTH),
        1
    )

    private val tvYear: TextView

    private val tvMonth: TextView

    private val rvCalendar: RecyclerView

    private val tvClear: TextView

    private val swContinue: Switch

    private lateinit var yearPicker: YearPicker

    private lateinit var monthPicker: MonthPicker

    private lateinit var adapter: DateAdapter

    init {
        val view = View.inflate(context, R.layout.layout_calendar, null)
        val calendar = Calendar.getInstance()

        tvYear = view.findViewById(R.id.tv_year)
        tvMonth = view.findViewById(R.id.tv_month)
        rvCalendar = view.findViewById(R.id.rv_calendar)
        tvClear = view.findViewById(R.id.tv_clear)
        swContinue = view.findViewById(R.id.sw_continue)

        swContinue.setOnCheckedChangeListener { _, isCheck ->
            if (isCheck) {
                CustomCalendarManager.isContinue = true
                adapter.isContinue = true
            } else {
                CustomCalendarManager.isContinue = true
                adapter.isContinue = true
            }
        }


        //set year
        calendar.set(Calendar.YEAR, mDateModel.year)
        //set month
        calendar.set(Calendar.MONTH, mDateModel.month)
        //set date (need to be fixed by 1, or there will calculate incorrectly)
        calendar.set(Calendar.DAY_OF_MONTH, 1)

        tvYear.setOnClickListener {
            val yearDialog = YearDialog()
            yearDialog.show((context as FragmentActivity).supportFragmentManager, null)
            yearDialog.setOnYearPickerListener(object : YearDialog.OnYearPickerListener {
                override fun onYear(year: Int) {
                    calendar.set(Calendar.YEAR, year)
                    setDateRecyclerView(calendar.get(Calendar.MONTH), year)
                    yearPicker.onYearPick(year)
                }
            })
        }

        tvMonth.setOnClickListener {
            val monthDialog = MonthDialog()
            monthDialog.show((context as FragmentActivity).supportFragmentManager, null)
            monthDialog.setOnMonthPickerListener(object : MonthDialog.OnMonthPickerListener {
                override fun onMonth(month: Int) {
                    monthPicker.onMonthPicker(month)
                }
            })
        }

        tvClear.setOnClickListener {
            adapter.clearPickDates()
            CustomCalendarManager.pickDates.clear()
            notifyDataOverMonth()
            CustomCalendarManager.notifyPositions.clear()
        }

        rvCalendar.layoutManager = GridLayoutManager(context, 7)
        this.addView(view)
    }

    fun setYear(year: Int) {
        tvYear.text = "$year"
    }

    fun setMonth(month: Int) {
        tvMonth.text = Calendar.getInstance().apply { set(Calendar.MONTH, month) }
            .getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    }

    fun setDateRecyclerView(month: Int, year: Int = Calendar.getInstance().get(Calendar.YEAR)) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.DATE, 1)
        adapter = DateAdapter(context, calendar, calendar.get(Calendar.YEAR), month)
        rvCalendar.adapter = adapter
    }

    fun setDateListener(dateListener: DateAdapter.DateListener) {
        adapter.setDateListener(dateListener)
    }


    interface YearPicker {
        fun onYearPick(year: Int)
    }

    fun setYearPickerListener(yearPicker: YearPicker) {
        this.yearPicker = yearPicker
    }

    interface MonthPicker {
        fun onMonthPicker(month: Int)
    }

    fun setMonthPickerListener(monthPicker: MonthPicker) {
        this.monthPicker = monthPicker
    }

    fun setSwithContinue(isCheck: Boolean) {
        swContinue.isChecked = isCheck
    }

    private fun notifyDataOverMonth() {
        CustomCalendarManager.notifyPositions.sort()
        if (CustomCalendarManager.notifyPositions.size == 1) {
            val month = CustomCalendarManager.notifyPositions[0]
            CustomCalendarManager.viewPager.findViewWithTag<CustomCalendarView>(month)
                .rv_calendar.adapter!!.notifyDataSetChanged()
        } else if (CustomCalendarManager.notifyPositions.size == 2) {
            val startMonth = CustomCalendarManager.notifyPositions[0]
            val endMonth = CustomCalendarManager.notifyPositions[1]
            for (month in startMonth..endMonth) {
                CustomCalendarManager.viewPager.findViewWithTag<CustomCalendarView>(month)
                    .rv_calendar.adapter!!.notifyDataSetChanged()
            }
        }
    }

}
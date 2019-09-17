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

    private val rvCalendar: RecyclerView

    private lateinit var adapter: DateAdapter

    init {
        val view = View.inflate(context, R.layout.layout_calendar, null)
        val calendar = Calendar.getInstance()

        rvCalendar = view.findViewById(R.id.rv_calendar)

        //set year
        calendar.set(Calendar.YEAR, mDateModel.year)
        //set month
        calendar.set(Calendar.MONTH, mDateModel.month)
        //set date (need to be fixed by 1, or there will calculate incorrectly)
        calendar.set(Calendar.DAY_OF_MONTH, 1)


        rvCalendar.layoutManager = GridLayoutManager(context, 7)
        this.addView(view)
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

}
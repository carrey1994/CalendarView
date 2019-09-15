package com.example.customcalendarview

import android.annotation.SuppressLint
import android.content.Context
import android.text.format.DateUtils
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class DateAdapter(
    private val context: Context,
    private val mCalendar: Calendar,
    private val year: Int,
    private val month: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_OFF = 0
    private val TYPE_ON = 1
    private val TYPE_TODAY = 2

    private val dateOffset = mCalendar.get(Calendar.DAY_OF_WEEK) - 1

    private val pickDates = arrayListOf<Long>()

    private lateinit var dateListener: DateListener

    override fun getItemViewType(position: Int): Int {
        val type: Int
        val currentMonthRange = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        mCalendar.add(Calendar.DATE, position - dateOffset)
        type = when {
            DateUtils.isToday(mCalendar.timeInMillis) && (position in dateOffset until currentMonthRange + dateOffset) -> TYPE_TODAY
            position in dateOffset until currentMonthRange + dateOffset ->
                //需判斷跨月後範圍增大的問題
                TYPE_ON
            else -> TYPE_OFF
        }
        resetCalendar()
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var dateViewHolder: RecyclerView.ViewHolder
        when (viewType) {
            TYPE_ON -> {
                dateViewHolder = DateViewHolder(View.inflate(context, R.layout.item_date, null))
            }
            TYPE_OFF -> {
                dateViewHolder =
                    NonDateViewHolder(View.inflate(context, R.layout.item_non_date, null))
            }
            TYPE_TODAY -> {
                dateViewHolder =
                    TodayViewHolder(View.inflate(context, R.layout.item_today, null))
            }
        }
        return dateViewHolder
    }

    override fun getItemCount(): Int =
//        if (dateOffset == 6 || (dateOffset==5 && mCalendar.get(Calendar.DAY_OF_MONTH)==31)) 42 else 35
        42


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is TodayViewHolder -> {
                mCalendar.add(Calendar.DATE, position - dateOffset)
                holder.tvDate.text = mCalendar.get(Calendar.DATE).toString()
                val dateText = "${holder.tvDate.text.toString().toInt()}/${month + 1}/$year"
                switchBackground(holder.flDate, dateText)

                holder.tvDate.setOnClickListener {
                    if (pickDates.size < 2) {
                        it.isSelected = !it.isSelected
                        dateListener.onDate(year, month + 1, holder.tvDate.text.toString().toInt())
                        pickDates.add(getLongFromDate("${holder.tvDate.text.toString().toInt()}/${month + 1}/$year"))
                        holder.flDate.isSelected = it.isSelected
                        switchBackground(holder.flDate, dateText)
                        notifyDataSetChanged()
                    }
                }
            }
            is DateViewHolder -> {
                mCalendar.add(Calendar.DATE, position - dateOffset)
                holder.tvDate.text = mCalendar.get(Calendar.DATE).toString()
                val dateText = "${holder.tvDate.text.toString().toInt()}/${month + 1}/$year"
                switchBackground(holder.flDate, dateText)

                holder.tvDate.setOnClickListener {
                    if (pickDates.size < 2) {
                        it.isSelected = !it.isSelected
                        dateListener.onDate(year, month + 1, holder.tvDate.text.toString().toInt())
                        pickDates.add(getLongFromDate("${holder.tvDate.text.toString().toInt()}/${month + 1}/$year"))
                        holder.flDate.isSelected = it.isSelected
                        switchBackground(holder.flDate, dateText)
                        notifyDataSetChanged()
                    }
                }
            }
            is NonDateViewHolder -> {
                mCalendar.add(Calendar.DATE, -dateOffset + position)
                holder.tvDate.text = mCalendar.get(Calendar.DATE).toString()
//                holder.flDate.background = ContextCompat.getDrawable(context, R.drawable.selector_mid_shape)
            }

        }

        resetCalendar()
    }

    private fun resetCalendar() {
        //recover calendar to be original
        mCalendar.set(Calendar.YEAR, year)
        //set month
        mCalendar.set(Calendar.MONTH, month)
        //set date (need to be fixed by 1, or there will calculate incorrectly)
        mCalendar.set(Calendar.DATE, 1)
    }

    inner class DateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView by lazy {
            view.findViewById<TextView>(R.id.tv_date)
        }
        val flDate: FrameLayout by lazy {
            view.findViewById<FrameLayout>(R.id.fl_date)
        }
    }

    inner class NonDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView by lazy {
            view.findViewById<TextView>(R.id.tv_date)
        }
        val flDate: FrameLayout by lazy {
            view.findViewById<FrameLayout>(R.id.fl_date)
        }
    }

    inner class TodayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView by lazy {
            view.findViewById<TextView>(R.id.tv_date)
        }
        val flDate: FrameLayout by lazy {
            view.findViewById<FrameLayout>(R.id.fl_date)
        }
    }

    interface DateListener {
        fun onDate(year: Int, month: Int, date: Int)
    }

    fun setDateListener(dateListener: DateListener) {
        this.dateListener = dateListener
    }

    fun clearPickDates() {
        pickDates.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat")
    fun getLongFromDate(dateText: String): Long {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val dateTime = dateFormat.parse(dateText)
        return dateTime!!.time

    }

    private fun switchBackground(bgLayout: FrameLayout, dateText: String) {
        pickDates.sort()
        if (pickDates.isEmpty())
            bgLayout.isSelected = false

        if (pickDates.size == 1 && getLongFromDate(dateText) == pickDates.first()) {
            bgLayout.isSelected = true
            bgLayout.background =
                ContextCompat.getDrawable(context, R.drawable.shape_circle)
        } else if (pickDates.isNotEmpty())
            when {
                getLongFromDate(dateText) == pickDates.first() -> {
                    bgLayout.isSelected = true
                    bgLayout.background = ContextCompat.getDrawable(
                        context, R.drawable.selector_left_shape
                    )
                }
                getLongFromDate(dateText) == pickDates.last() -> {
                    bgLayout.isSelected = true
                    bgLayout.background = ContextCompat.getDrawable(
                        context, R.drawable.selector_right_shape
                    )
                }
                (getLongFromDate(dateText) in pickDates.first()..pickDates.last()) -> {
                    bgLayout.isSelected = true
                    bgLayout.background = ContextCompat.getDrawable(
                        context, R.drawable.selector_mid_shape
                    )
                }
            }
    }

}
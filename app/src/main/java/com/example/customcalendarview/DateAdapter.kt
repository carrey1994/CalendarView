package com.example.customcalendarview

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
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

    private val pickDates = arrayListOf<Int>()

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
                val fn = {
                    pickDates.sort()
                    if (pickDates.isEmpty())
                        holder.clDate.isSelected = false
                    else if (pickDates.size == 1 && pickDates.first() == holder.tvDate.text.toString().toInt()) {
                        holder.clDate.isSelected = true
                        holder.clDate.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_circle)
                    } else
                        if (pickDates.isNotEmpty())
                            when {
                                holder.tvDate.text.toString().toInt() == pickDates.first() -> {
                                    Log.e("pickDates.first()===", "${pickDates.first()}")
                                    holder.clDate.isSelected = true
                                    holder.clDate.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.selector_left_shape
                                        )
                                }
                                holder.tvDate.text.toString().toInt() == pickDates.last() -> {
                                    Log.e("pickDates.last()===", "${pickDates.last()}")
                                    holder.clDate.isSelected = true
                                    holder.clDate.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.selector_right_shape
                                        )
                                }
                                (holder.tvDate.text.toString().toInt() in pickDates.first()..pickDates.last()) -> {
                                    Log.e("pickDates.else()===", "${holder.tvDate.text}")
                                    holder.clDate.isSelected = true
                                    holder.clDate.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.selector_mid_shape
                                        )
                                }
                            }
                }
                fn()
                holder.tvDate.setOnClickListener {
                    it.isSelected = !it.isSelected
                    dateListener.onDate(year, month + 1, holder.tvDate.text.toString().toInt())
                    pickDates.add(holder.tvDate.text.toString().toInt())
                    holder.clDate.isSelected = it.isSelected
                    fn()
                    notifyDataSetChanged()
                }
            }
            is DateViewHolder -> {
                mCalendar.add(Calendar.DATE, position - dateOffset)
                holder.tvDate.text = mCalendar.get(Calendar.DATE).toString()

                val fn = {
                    pickDates.sort()
                    if (pickDates.size == 1 && holder.tvDate.text.toString().toInt() == pickDates.first()) {
                        holder.clDate.isSelected = true
                        holder.clDate.background =
                            ContextCompat.getDrawable(context, R.drawable.shape_circle)
                    } else
                        if (pickDates.isNotEmpty())
                            when {
                                holder.tvDate.text.toString().toInt() == pickDates.first() -> {
                                    Log.e("pickDates.first()===", "${pickDates.first()}")
                                    holder.clDate.isSelected = true
                                    holder.clDate.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.selector_left_shape
                                        )
                                }
                                holder.tvDate.text.toString().toInt() == pickDates.last() -> {
                                    Log.e("pickDates.last()===", "${pickDates.last()}")
                                    holder.clDate.isSelected = true
                                    holder.clDate.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.selector_right_shape
                                        )
                                }
                                (holder.tvDate.text.toString().toInt() in pickDates.first()..pickDates.last()) -> {
                                    Log.e("pickDates.else()===", "${holder.tvDate.text}")
                                    holder.clDate.isSelected = true
                                    holder.clDate.background =
                                        ContextCompat.getDrawable(
                                            context,
                                            R.drawable.selector_mid_shape
                                        )
                                }
                            }
                }

                fn()

                holder.tvDate.setOnClickListener {
                    it.isSelected = !it.isSelected
                    dateListener.onDate(year, month + 1, holder.tvDate.text.toString().toInt())
                    pickDates.add(holder.tvDate.text.toString().toInt())
                    holder.clDate.isSelected = it.isSelected
                    fn()
                    notifyDataSetChanged()
                }
            }
            is NonDateViewHolder -> {
                mCalendar.add(Calendar.DATE, -dateOffset + position)
                holder.tvDate.text = mCalendar.get(Calendar.DATE).toString()
//                holder.clDate.background = ContextCompat.getDrawable(context, R.drawable.selector_mid_shape)
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
        val clDate: FrameLayout by lazy {
            view.findViewById<FrameLayout>(R.id.fl_date)
        }
    }

    inner class NonDateViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView by lazy {
            view.findViewById<TextView>(R.id.tv_date)
        }
        val clDate: FrameLayout by lazy {
            view.findViewById<FrameLayout>(R.id.fl_date)
        }
    }

    inner class TodayViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvDate: TextView by lazy {
            view.findViewById<TextView>(R.id.tv_date)
        }
        val clDate: FrameLayout by lazy {
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

}
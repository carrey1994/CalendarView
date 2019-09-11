package com.example.customcalendarview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendarview.dialog.adapter.MonthAdapter
import com.example.customcalendarview.dialog.adapter.YearAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MonthDialog : BottomSheetDialogFragment() {

    private lateinit var rvMonth: RecyclerView
    private lateinit var monthPickerListener: OnMonthPickerListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_condition, container, false)
        rvMonth = view.findViewById(R.id.rv_condition)

        rvMonth.layoutManager = GridLayoutManager(context, 4)
        val yearAdapter = MonthAdapter(context!!)
        yearAdapter.setSelectMonthListener(object : MonthAdapter.SelectMonthListener {
            override fun onSelectMonth(month: Int) {
                monthPickerListener.onMonth(month)
                dismiss()
            }
        })
        rvMonth.adapter = yearAdapter
        return view
    }

    interface OnMonthPickerListener {
        fun onMonth(month: Int)
    }

    fun setOnMonthPickerListener(monthPickerListener: OnMonthPickerListener) {
        this.monthPickerListener = monthPickerListener
    }


}
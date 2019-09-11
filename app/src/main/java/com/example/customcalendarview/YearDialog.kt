package com.example.customcalendarview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.customcalendarview.dialog.adapter.YearAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class YearDialog : BottomSheetDialogFragment() {

    private lateinit var rvCondition: RecyclerView
    private lateinit var yearPickerListener: OnYearPickerListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.dialog_condition, container, false)
        rvCondition = view.findViewById(R.id.rv_condition)

        rvCondition.layoutManager = GridLayoutManager(context, 4)
        val yearAdapter = YearAdapter(context!!)
        yearAdapter.setSelectYearListener(object : YearAdapter.SelectYearListener {
            override fun onSelectYear(year: Int) {
                yearPickerListener.onYear(year)
                dismiss()
            }
        })
        rvCondition.adapter = yearAdapter
        return view
    }

    interface OnYearPickerListener {
        fun onYear(year: Int)
    }

    fun setOnYearPickerListener(yearPickerListener: OnYearPickerListener) {
        this.yearPickerListener = yearPickerListener
    }


}
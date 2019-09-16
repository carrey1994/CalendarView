package com.example.customcalendarview

import androidx.viewpager.widget.ViewPager

object CustomCalendarManager {

    var isContinue = true

    val pickDates = arrayListOf<Long>()

    lateinit var viewPager: ViewPager

    val notifyPositions = arrayListOf<Int>()


}
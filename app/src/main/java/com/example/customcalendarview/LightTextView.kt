package com.example.customcalendarview

import android.content.Context
import android.graphics.BlurMaskFilter
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class LightTextView : AppCompatTextView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        paint.maskFilter = BlurMaskFilter(2F, BlurMaskFilter.Blur.OUTER)

    }
}
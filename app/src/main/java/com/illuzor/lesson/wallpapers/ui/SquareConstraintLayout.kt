package com.illuzor.lesson.wallpapers.ui

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout

class SquareConstraintLayout(context: Context?, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {
    override fun onMeasure(width: Int, height: Int) = super.onMeasure(width, width)
}

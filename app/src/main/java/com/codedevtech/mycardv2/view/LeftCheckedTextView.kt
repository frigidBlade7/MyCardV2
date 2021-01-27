package com.codedevtech.mycardv2.view

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity

class LeftCheckedTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) :
    androidx.appcompat.widget.AppCompatCheckedTextView(context, attrs, defStyleAttr) {

    private val mCheckMarkGravity = Gravity.START

}
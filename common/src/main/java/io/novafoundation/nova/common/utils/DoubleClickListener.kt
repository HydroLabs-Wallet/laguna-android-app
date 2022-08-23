package io.novafoundation.nova.common.utils

import android.util.Log
import android.view.View
abstract class DoubleClickListener : View.OnClickListener {
    private var lastClickTime: Long = 0
    override fun onClick(v: View) {
        val clickTime = System.currentTimeMillis()
        if (clickTime - lastClickTime < DOUBLE_CLICK_TIME_DELTA) {
            onDoubleClick(v)
            lastClickTime = 0
        }
        Log.e("mcheck","delta ${clickTime-lastClickTime}")
        lastClickTime = clickTime
    }
    abstract fun onDoubleClick(v: View)
    companion object {
        private const val DOUBLE_CLICK_TIME_DELTA: Long = 300 //milliseconds
    }
}

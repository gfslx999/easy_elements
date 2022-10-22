package com.gfs.helper.elements

import android.util.Log

internal object ElementsLogUtil {

    private const val TAG = "ElementsLogUtil"

    fun logI(msg: Any?) {
        Log.i(TAG, "logI: $msg")
    }

    fun logE(msg: Any?) {
        Log.e(TAG, "logE: $msg")
    }

}
package com.manchaw.restaurant.util


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View

import com.google.android.material.snackbar.Snackbar
import com.manchaw.restaurant.BuildConfig

fun isConnected(mContext: Context?): Boolean {
    if (mContext != null) {
        val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val info = connectivityManager.activeNetworkInfo
        return info != null && info.isConnected
    } else {
        return false
    }
}

fun debugLog(TAG: String, log: String) {
    if (BuildConfig.DEBUG) {
        Log.d(TAG, log)
    }
}

fun showSnackbar(v: View, msg: String) {
    Snackbar.make(v, msg, Snackbar.LENGTH_LONG).show()
}


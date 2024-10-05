package com.victor.projectwhatsapp.utils


import android.view.View
import android.widget.ProgressBar

object ViewUtils {
    fun progressBarVisible(progressBar: ProgressBar) {
            progressBar.visibility = View.VISIBLE
    }

    fun progressBarGone(progressBar: ProgressBar) {
            progressBar.visibility = View.GONE
    }
}
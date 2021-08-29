package com.searchinrecyclerviewexample.utils

import android.view.View

class ViewExtension {

    fun View.hide() {
        this.visibility = View.GONE
    }

    fun View.show() {
        this.visibility = View.VISIBLE
    }
}
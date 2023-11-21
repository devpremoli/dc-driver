package com.cmtelematics.cmtreferenceapp.common.ui.util

import android.content.Context
import android.content.ContextWrapper
import android.util.TypedValue
import androidx.appcompat.app.AppCompatActivity

fun Context.getActivity(): AppCompatActivity? {
    var currentContext = this

    while (currentContext is ContextWrapper) {
        if (currentContext is AppCompatActivity) {
            return currentContext
        }

        currentContext = currentContext.baseContext
    }

    return null
}

fun Context.requireActivity(): AppCompatActivity = requireNotNull(getActivity())

fun Context.resolveAttribute(attr: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attr, typedValue, true)
    return typedValue.resourceId
}

package com.cmtelematics.cmtreferenceapp.common.service

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class ToastServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ToastService {
    override fun toastShort(resource: Int) {
        Toast.makeText(context, resource, Toast.LENGTH_SHORT).show()
    }
}

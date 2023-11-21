package com.cmtelematics.cmtreferenceapp.settings.util

import android.content.ClipData
import android.content.ClipboardManager
import com.cmtelematics.cmtreferenceapp.common.service.ToastService
import com.cmtelematics.cmtreferenceapp.settings.R

internal fun copyToClipboard(clipboard: ClipboardManager, toastService: ToastService, text: String) {
    clipboard.setPrimaryClip(ClipData.newPlainText(null, text))
    toastService.toastShort(R.string.toast_copied)
}

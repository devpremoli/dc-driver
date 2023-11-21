package com.cmtelematics.cmtreferenceapp.common.service

import androidx.annotation.StringRes

/**
 * Allows creating toast messages from ViewModels.
 */
interface ToastService {
    /**
     * Create and show a Toast message to the user with a short duration.
     *
     * @param resource string resource to look up and show
     */
    fun toastShort(@StringRes resource: Int)
}

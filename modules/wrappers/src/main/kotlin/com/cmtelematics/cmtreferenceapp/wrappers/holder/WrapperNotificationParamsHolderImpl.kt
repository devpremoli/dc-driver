package com.cmtelematics.cmtreferenceapp.wrappers.holder

import com.cmtelematics.cmtreferenceapp.wrappers.service.model.WrapperNotificationParams
import javax.inject.Inject

internal class WrapperNotificationParamsHolderImpl @Inject constructor() : WrapperNotificationParamsHolder {
    override var notificationParams: WrapperNotificationParams? = null
}

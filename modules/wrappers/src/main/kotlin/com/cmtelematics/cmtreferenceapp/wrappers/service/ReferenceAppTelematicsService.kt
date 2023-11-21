package com.cmtelematics.cmtreferenceapp.wrappers.service

import com.cmtelematics.cmtreferenceapp.wrappers.holder.WrapperNotificationParamsHolder
import com.cmtelematics.sdk.CmtService
import com.cmtelematics.sdk.CmtServiceListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Subclass of the CMT Android Service.
 * Customize user-visible elements like titles and icons in listener.
 */
@AndroidEntryPoint
internal class ReferenceAppTelematicsService : CmtService() {
    @Inject
    internal lateinit var paramsHolder: WrapperNotificationParamsHolder

    override fun createListener(context: CmtService): CmtServiceListener = ReferenceAppTelematicsListener(
        context = context,
        notificationParams = paramsHolder.notificationParams
            ?: error(
                """
                Telematics service was started before notification params were specified.
                Was SDK properly initialized?
                """.trimIndent()
            )
    )
}

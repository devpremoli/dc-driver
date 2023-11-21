package com.cmtelematics.cmtreferenceapp.wrappers.sdk

import android.content.ComponentName
import android.content.Context
import com.cmtelematics.cmtreferenceapp.wrappers.service.ReferenceAppTelematicsService
import com.cmtelematics.cmtreferenceapp.wrappers.service.ServiceAnomalyReceiver
import com.cmtelematics.sdk.types.ServiceConfiguration

/**
 * [Docs](https://my-cmt-alpha.cmtelematics.com/sdkdoc/tech_pubs_cmt/nextgen_help_center/en/drivewell-sdk/drivewell-sdk-for-android/setting-up-the-drivewell-sdk-for-android/instantiating-the-data-model-object--android-.html)
 */
internal class ReferenceAppServiceConfiguration(
    private val context: Context,
    private val sdkConfiguration: SdkManager.SdkConfiguration
) : ServiceConfiguration() {
    override fun getCmtApiKey(): String = sdkConfiguration.apiKey

    override fun getEndpoint(): String = sdkConfiguration.endpoint

    override fun getTripRecordingService(): ComponentName =
        ComponentName(context, ReferenceAppTelematicsService::class.java)

    override fun getAnomalyReceiver(): ComponentName =
        ComponentName(context, ServiceAnomalyReceiver::class.java)

    override fun isReleaseMode(): Boolean = false
}

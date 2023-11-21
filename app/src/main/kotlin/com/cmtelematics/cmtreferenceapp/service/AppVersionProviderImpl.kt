package com.cmtelematics.cmtreferenceapp.service

import com.cmtelematics.cmtreferenceapp.common.di.qualifier.VersionCode
import com.cmtelematics.cmtreferenceapp.common.di.qualifier.VersionName
import com.cmtelematics.cmtreferenceapp.common.service.AppVersionProvider
import javax.inject.Inject

class AppVersionProviderImpl @Inject constructor(
    @VersionName versionName: String,
    @VersionCode versionCode: Int
) : AppVersionProvider {
    override val appVersion: String = versionName

    override val appVersionWithBuildNumber: String = "$versionName ($versionCode)"
}

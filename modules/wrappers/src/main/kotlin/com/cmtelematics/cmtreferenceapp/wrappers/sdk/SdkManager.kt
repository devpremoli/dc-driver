package com.cmtelematics.cmtreferenceapp.wrappers.sdk

import android.app.Application
import android.os.Bundle
import com.cmtelematics.cmtreferenceapp.wrappers.service.model.WrapperNotificationParams
import com.cmtelematics.sdk.AppModel

/**
 * Main utility entrypoint and wrapper around the DriveWell SDK.
 */
interface SdkManager {

    /**
     * Initialize the SDK. This should be called before any other SDK function is accessed.
     * It is highly recommended to call this in Application.onCreate()
     */
    fun initialize(application: Application, configuration: SdkConfiguration)

    // The SDK needs to be notified of Activity lifecycle events, regardless of whether the SDK is hosted in an
    // Activity or the Application instance.

    /**
     * Notify the SDK that the hosting Activity has started.
     */
    fun onActivityStarted()

    /**
     * Notify the SDK that the hosting Activity has resumed.
     */
    fun onActivityResumed()

    /**
     * Notify the SDK that the hosting Activity has paused.
     */
    fun onActivityPaused()

    /**
     * Notify the SDK that the hosting Activity needs to save instance state.
     */
    fun onActivitySaveInstanceState(outState: Bundle)

    /**
     * Notify the SDK that the hosting Activity has stopped.
     */
    fun onActivityStopped()

    /**
     * Notify the SDK that the hosting Activity been destroyed.
     */
    fun onActivityDestroyed()

    /**
     * Notify the SDK that it needs to trim memory.
     */
    fun onTrimMemory(level: Int)

    /**
     * Additional configuration values needed by the SDK for initialization.
     */
    data class SdkConfiguration(
        /**
         * The backend url. Should be https, or a MockWebServer url.
         */
        val endpoint: String,

        /**
         * The corresponding API Key
         */
        val apiKey: String,

        /**
         * Configuration parameters needed to configure Notifications.
         */
        val notificationParams: WrapperNotificationParams,

        /**
         * Whether to enable extra debugging facilities, like verbose logging and SDK Toast messages.
         */
        val debug: Boolean = false
    )
}

/**
 * Internal interface used to retrieve the [AppModel] from the sdk manager. Should not be accessible by other
 * modules.
 */
internal interface AppModelProvider {
    /**
     * The initialized [AppModel]. Can only be retrieved after the SDK has been initialized.
     */
    val appModel: AppModel
}

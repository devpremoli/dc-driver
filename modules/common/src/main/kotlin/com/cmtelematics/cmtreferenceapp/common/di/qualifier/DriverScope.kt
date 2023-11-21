package com.cmtelematics.cmtreferenceapp.common.di.qualifier

import javax.inject.Qualifier

/**
 * Qualifier for a coroutine scope meant for tasks that run continuously for the lifetime of the application.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class DriverScope

package com.cmtelematics.cmtreferenceapp.wrappers.di.qualifier

import com.cmtelematics.cmtreferenceapp.common.di.qualifier.DriverScope
import javax.inject.Qualifier

/**
 * Qualifier for a coroutine scope meant exclusively to host Flow sharing coroutines. Functionally identical to
 * [DriverScope] but used for a different purpose and may change in the future.
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
internal annotation class SharingScope

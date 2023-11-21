package com.cmtelematics.cmtreferenceapp.common.di.qualifier

import javax.inject.Qualifier

/**
 * Qualifier for a DataStore, which keeps the stored data after logout.
 */
@Qualifier
annotation class PermanentDataStore

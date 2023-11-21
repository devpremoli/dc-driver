package com.cmtelematics.cmtreferenceapp.common.di.qualifier

import javax.inject.Qualifier

/**
 * Qualifier for a DataStore, which does not keep the stored data after logout,
 * just until the expiration of user's session.
 */
@Qualifier
annotation class SessionDataStore

package com.cmtelematics.cmtreferenceapp.wrappers.authentication

/**
 * Exception thrown from [AuthenticationManager.logout] or [AuthenticationManager.refreshProfile] function
 * when user was logged out, did not have a valid session.
 */
class UserNotAuthenticatedException : Exception("User is not authenticated!")

package com.cmtelematics.cmtreferenceapp.wrappers.authentication

/**
 * Exception thrown from [AuthenticationManager.register]
 * or [AuthenticationManager.verify]
 * or [AuthenticationManager.loginWithOTP] function when user was logged in, had a valid session.
 */
class UserAlreadyAuthenticatedException : Exception("User is already authenticated!")

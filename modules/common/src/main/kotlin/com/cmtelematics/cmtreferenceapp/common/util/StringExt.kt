package com.cmtelematics.cmtreferenceapp.common.util

private val emailRegex = Regex("^[\\w-.]+(\\+[\\w-.]+)?@([\\w-]+\\.)+[\\w-]{2,4}\$")

/**
 * Returns true if this string is represents a valid email address.
 */
fun String.isValidEmail(): Boolean = emailRegex.matches(this)

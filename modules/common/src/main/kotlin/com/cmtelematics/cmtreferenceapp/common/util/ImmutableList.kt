package com.cmtelematics.cmtreferenceapp.common.util

import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

fun <T> Array<T>.toImmutableList(): ImmutableList<T> = toList().toImmutableList()

fun <T> emptyImmutableList(): ImmutableList<T> = emptyList<T>().toImmutableList()

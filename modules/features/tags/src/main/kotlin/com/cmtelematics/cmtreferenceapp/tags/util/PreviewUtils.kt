package com.cmtelematics.cmtreferenceapp.tags.util

import com.cmtelematics.sdk.types.TagSummary
import com.cmtelematics.sdk.types.Version

@Suppress("MagicNumber")
internal fun makeDummyTag() = TagSummary(
    "mac address",
    true,
    Version(1, 1, 1, 1),
    Version(1, 1, 1, 1),
    50,
    1_000,
    42,
    5,
    8,
    false
)

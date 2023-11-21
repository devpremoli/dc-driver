package com.cmtelematics.cmtreferenceapp.authentication.util

import android.text.SpannedString
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

@Composable
fun spannedStringResource(
    @StringRes textResourceId: Int
): SpannedString = SpannedString(LocalContext.current.resources.getText(textResourceId))

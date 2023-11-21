package com.cmtelematics.cmtreferenceapp.theme.ui

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.runtime.Composable

@Composable
internal fun appTextSelectionColors() = TextSelectionColors(
    handleColor = AppTheme.colors.primary,
    backgroundColor = AppTheme.colors.primary.copy(alpha = 0.4F)
)

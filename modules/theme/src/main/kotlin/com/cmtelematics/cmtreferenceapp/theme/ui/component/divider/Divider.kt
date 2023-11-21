package com.cmtelematics.cmtreferenceapp.theme.ui.component.divider

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import androidx.compose.material.Divider as MaterialDivider

@Composable
fun Divider(
    modifier: Modifier = Modifier,
    color: Color = AppTheme.colors.background.primary
) {
    MaterialDivider(
        modifier = modifier,
        color = color
    )
}

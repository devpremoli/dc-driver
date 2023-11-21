package com.cmtelematics.cmtreferenceapp.theme.ui.component.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun ScreenScaffold(
    modifier: Modifier = Modifier,
    backgroundColor: Color = AppTheme.colors.background.primary,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    toolbar: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    ScreenScaffold(
        modifier = modifier,
        backgroundBrush = SolidColor(backgroundColor),
        horizontalAlignment = horizontalAlignment,
        toolbar = toolbar,
        content = content
    )
}

@Composable
private fun ScreenScaffold(
    modifier: Modifier = Modifier,
    backgroundBrush: Brush = SolidColor(AppTheme.colors.background.primary),
    horizontalAlignment: Alignment.Horizontal = Alignment.Start,
    toolbar: @Composable (() -> Unit)? = null,
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush)
    ) {
        if (toolbar != null) {
            toolbar()
        } else {
            Spacer(
                modifier = Modifier
                    .windowInsetsTopHeight(WindowInsets.statusBars)
                    .fillMaxWidth()
            )
        }

        Column(
            modifier = modifier.fillMaxSize(),
            horizontalAlignment = horizontalAlignment,
            content = content
        )
    }
}

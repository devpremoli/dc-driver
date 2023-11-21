package com.cmtelematics.cmtreferenceapp.ui.component

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
internal fun SetupStatusBar(systemUIColor: SystemUIColorPalette) {
    val systemUiController = rememberSystemUiController()

    val color: Color = when (systemUIColor) {
        SystemUIColorPalette.Default -> Color.Transparent
        SystemUIColorPalette.Splash -> AppTheme.colors.primary
    }

    val shouldUseDarkIcons: Boolean = when (systemUIColor) {
        SystemUIColorPalette.Default -> MaterialTheme.colors.isLight
        SystemUIColorPalette.Splash -> false
    }

    SideEffect {
        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = shouldUseDarkIcons
        )
    }
}

internal enum class SystemUIColorPalette {
    Default, Splash
}

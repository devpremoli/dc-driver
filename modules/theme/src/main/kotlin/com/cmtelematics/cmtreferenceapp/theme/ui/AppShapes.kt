package com.cmtelematics.cmtreferenceapp.theme.ui

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Immutable
data class AppShapes(
    val button: Shape = RoundedCornerShape(4.dp),
    val card: Shape = RoundedCornerShape(2.dp),
    val dialog: Shape = RoundedCornerShape(4.dp),
    val circle: Shape = CircleShape,
    val bottomNavigation: Shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    val menuMoreBackground: Shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp),
    val snackbar: Shape = RoundedCornerShape(8.dp),
    val progressBar: Shape = RoundedCornerShape(100.dp)
)

internal val LocalAppShapes = staticCompositionLocalOf { AppShapes() }

package com.cmtelematics.cmtreferenceapp.theme.ui.component.progressindicator

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color,
    percentage: Float,
    strokeWidth: Dp,
    startAngle: Float = DEFAULT_START_ANGLE
) {
    Canvas(modifier = modifier.aspectRatio(1f)) {
        drawArc(
            color = color,
            startAngle = startAngle,
            sweepAngle = FULL_CIRCLE_ANGLE * percentage,
            useCenter = false,
            style = Stroke(
                width = strokeWidth.toPx(),
                cap = StrokeCap.Round
            )
        )
    }
}

private const val DEFAULT_START_ANGLE = -90f
private const val FULL_CIRCLE_ANGLE = 360

@Preview
@Composable
private fun CircularProgressIndicatorPreview() {
    AppTheme {
        CircularProgressIndicator(
            color = AppTheme.colors.gray.grayMedium,
            percentage = 0.5f,
            strokeWidth = 16.dp
        )
    }
}

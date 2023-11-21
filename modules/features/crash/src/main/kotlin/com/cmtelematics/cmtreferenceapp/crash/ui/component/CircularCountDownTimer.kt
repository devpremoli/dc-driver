package com.cmtelematics.cmtreferenceapp.crash.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.progressindicator.CircularProgressIndicator

@Composable
internal fun CircularCountDownTimer(
    modifier: Modifier,
    remainingCountDownTime: Int,
    remainingCountDownTimeProgress: Float,
    strokeWidth: Dp = DEFAULT_STROKE_WIDTH
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        CircularProgressIndicator(
            color = AppTheme.colors.gray.grayLight,
            strokeWidth = strokeWidth,
            percentage = 1f
        )

        CircularProgressIndicator(
            color = AppTheme.colors.gray.grayMedium,
            strokeWidth = strokeWidth,
            percentage = remainingCountDownTimeProgress
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = remainingCountDownTime.coerceAtLeast(0).toString(),
                fontWeight = FontWeight.Bold,
                fontSize = SECOND_TEXT_FONT_SIZE
            )

            Text(
                text = stringResource(R.string.crash_detected_seconds_text),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                fontSize = AppTheme.dimens.fontSize.textL
            )
        }
    }
}

private val DEFAULT_STROKE_WIDTH = 16.dp
private val SECOND_TEXT_FONT_SIZE = 84.sp

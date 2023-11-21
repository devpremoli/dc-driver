package com.cmtelematics.cmtreferenceapp.crash.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun CrashTitleText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        style = AppTheme.typography.title.xLarge
    )
}

@Composable
internal fun CrashSubTitleText(
    modifier: Modifier = Modifier,
    text: String
) {
    Text(
        modifier = modifier.fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        style = AppTheme.typography.text.large,
        color = AppTheme.colors.gray.grayDark
    )
}

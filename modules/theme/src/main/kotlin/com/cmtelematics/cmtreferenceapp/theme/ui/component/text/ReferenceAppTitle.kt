package com.cmtelematics.cmtreferenceapp.theme.ui.component.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.cmtelematics.cmtreferenceapp.theme.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun ReferenceAppTitle(
    modifier: Modifier = Modifier
) = Text(
    stringResource(id = R.string.sdk_app_title),
    modifier = modifier,
    style = AppTheme.typography.title.xLarge,
    color = AppTheme.colors.background.primary,
    textAlign = TextAlign.Center
)

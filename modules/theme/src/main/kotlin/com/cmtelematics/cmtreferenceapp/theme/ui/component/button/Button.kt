package com.cmtelematics.cmtreferenceapp.theme.ui.component.button

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTypography

@Composable
internal fun buttonSizeToMinHeight(buttonSize: ButtonSize) = when (buttonSize) {
    ButtonSize.Large -> AppTheme.dimens.button.minHeightLarge
    ButtonSize.Medium -> AppTheme.dimens.button.minHeightNormal
}

@Composable
internal fun buttonSizeToPadding(buttonSize: ButtonSize): PaddingValues = when (buttonSize) {
    ButtonSize.Large -> PaddingValues(
        horizontal = AppTheme.dimens.button.horizontalPaddingLarge,
        vertical = AppTheme.dimens.button.verticalPaddingLarge
    )
    ButtonSize.Medium -> PaddingValues(
        horizontal = AppTheme.dimens.button.horizontalPaddingNormal,
        vertical = AppTheme.dimens.button.verticalPaddingNormal
    )
}

@Composable
internal fun buttonSizeStyle(buttonSize: ButtonSize): AppTypography.Button.Style =
    when (buttonSize) {
        ButtonSize.Large -> AppTheme.typography.button.large
        ButtonSize.Medium -> AppTheme.typography.button.large
    }

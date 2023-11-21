package com.cmtelematics.cmtreferenceapp.crash.ui.component

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.OutlinedButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold

@Composable
internal fun CrashScreenScaffold(
    modifier: Modifier = Modifier,
    primaryButtonClicked: (() -> Unit)? = null,
    primaryButtonText: String? = null,
    secondaryButtonClicked: (() -> Unit)? = null,
    secondaryButtonText: String? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    ScreenScaffold(modifier = modifier.padding(AppTheme.dimens.margin.roomy)) {
        content()

        primaryButtonClicked?.let { clickHandler ->
            PrimaryButton(
                modifier = Modifier.fillMaxWidth(),
                text = primaryButtonText ?: error("primaryButtonText must be defined"),
                onClick = clickHandler,
                buttonSize = ButtonSize.Large
            )
        }

        secondaryButtonClicked?.let { clickHandler ->
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.tiny))

            OutlinedButton(
                modifier = Modifier.fillMaxWidth(),
                text = secondaryButtonText ?: error("secondaryButtonText must be defined"),
                onClick = clickHandler,
                buttonSize = ButtonSize.Large
            )
        }
    }
}

@Preview
@Composable
private fun CrashDetectedScreenScaffoldPreview() {
    AppTheme {
        CrashScreenScaffold(
            modifier = Modifier,
            content = {},
            primaryButtonClicked = {},
            primaryButtonText = "YES",
            secondaryButtonClicked = {},
            secondaryButtonText = "NO"
        )
    }
}

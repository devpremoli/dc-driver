package com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.theme.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun Toolbar(
    title: String = "",
    modifier: Modifier = Modifier,
    action: @Composable (() -> Unit)? = null,
    secondaryAction: @Composable (() -> Unit)? = null
) {
    Column(modifier.fillMaxWidth()) {
        Spacer(
            modifier = modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .background(AppTheme.colors.background.surface)
        ) {
            Row(
                modifier = Modifier.align(Alignment.CenterStart),
                verticalAlignment = Alignment.CenterVertically
            ) {
                action?.let { it() } ?: Spacer(modifier = Modifier.width(AppTheme.dimens.margin.roomy))

                Text(text = title, style = AppTheme.typography.title.medium)

                secondaryAction?.let { RightActionButtonSection(modifier, it) }
            }
        }
    }
}

@Composable
private fun RowScope.RightActionButtonSection(modifier: Modifier, action: @Composable () -> Unit) {
    Spacer(modifier = modifier.weight(1f))

    action()
}

@Composable
fun ToolbarBackButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    ToolbarButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_back_arrow),
            contentDescription = null
        )
    }
}

@Composable
fun ToolbarCloseButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true
) {
    ToolbarButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_cancel_icon),
            contentDescription = null
        )
    }
}

@Composable
private fun ToolbarButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.defaultMinSize(56.dp),
        enabled = enabled,
        shape = AppTheme.shapes.circle,
        colors = ButtonDefaults.buttonColors(
            contentColor = AppTheme.colors.background.content,
            backgroundColor = Color.Transparent,
            disabledBackgroundColor = Color.Transparent
        ),
        elevation = null,
        content = content
    )
}

@Preview
@Composable
private fun ToolbarPreview() {
    AppTheme {
        Toolbar(title = "Toolbar")
    }
}

@Preview
@Composable
private fun SubRouteToolbarPreview() {
    AppTheme {
        Toolbar(
            title = "Subroute",
            action = { ToolbarBackButton() }
        )
    }
}

@Preview
@Composable
private fun SubRouteToolbarPreviewWithSecondaryButton() {
    AppTheme {
        Toolbar(
            title = "Subroute",
            action = { ToolbarBackButton() },
            secondaryAction = { ToolbarCloseButton() }
        )
    }
}

@Preview
@Composable
private fun BackButtonPreview() {
    AppTheme {
        ToolbarBackButton()
    }
}

@Preview
@Composable
private fun CloseButtonPreview() {
    AppTheme {
        ToolbarCloseButton()
    }
}

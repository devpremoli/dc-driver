package com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun LoadingDialog() {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            dismissOnBackPress = false,
            usePlatformDefaultWidth = false
        ),
        content = { LoadingDialogContent() }
    )
}

@Composable
private fun LoadingDialogContent() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background.surface)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center),
            color = AppTheme.colors.primary
        )
    }
}

@Preview
@Composable
private fun LoadingDialogContentPreview() {
    AppTheme {
        LoadingDialogContent()
    }
}

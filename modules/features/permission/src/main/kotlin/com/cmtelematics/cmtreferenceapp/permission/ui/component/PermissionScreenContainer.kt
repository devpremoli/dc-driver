package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun PermissionRequesterScaffoldContainer(
    content: @Composable (ColumnScope.() -> Unit)
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppTheme.colors.background.primary)
            .padding(
                start = AppTheme.dimens.margin.bigger,
                end = AppTheme.dimens.margin.bigger,
                bottom = AppTheme.dimens.margin.bigger
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(
            modifier = Modifier
                .windowInsetsTopHeight(WindowInsets.statusBars)
                .fillMaxWidth()
        )

        content()
    }
}

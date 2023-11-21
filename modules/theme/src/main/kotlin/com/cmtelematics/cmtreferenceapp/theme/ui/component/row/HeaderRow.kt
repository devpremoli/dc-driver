package com.cmtelematics.cmtreferenceapp.theme.ui.component.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
fun HeaderRow(
    modifier: Modifier = Modifier,
    title: String
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppTheme.dimens.minTapSize)
            .padding(horizontal = AppTheme.dimens.margin.default),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = title,
            style = AppTheme.typography.text.small.merge(TextStyle(color = AppTheme.colors.text.tableDetails))
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun HeaderRowPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
                .background(color = AppTheme.colors.background.content)
        ) {
            HeaderRow(title = "EXAMPLE HEADER")
        }
    }
}

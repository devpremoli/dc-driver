package com.cmtelematics.cmtreferenceapp.theme.ui.component.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider

@Composable
fun SecondaryTextRow(
    modifier: Modifier = Modifier,
    title: String
) {
    Column(
        modifier = modifier.background(AppTheme.colors.background.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.minTapSize),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = AppTheme.typography.text.large.merge(TextStyle(color = AppTheme.colors.text.tableDetails))
            )
        }

        Divider(color = AppTheme.colors.divider)
    }
}

@Preview(showBackground = true)
@Composable
private fun SecondaryTextRowPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            SecondaryTextRow(title = "Example")
        }
    }
}

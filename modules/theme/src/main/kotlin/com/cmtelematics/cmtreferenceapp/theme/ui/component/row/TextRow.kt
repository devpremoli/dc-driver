package com.cmtelematics.cmtreferenceapp.theme.ui.component.row

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider

@Composable
fun TextRow(
    modifier: Modifier = Modifier,
    rowTitle: String,
    rowValue: String? = null
) {
    Column(
        modifier = modifier.background(AppTheme.colors.background.primary)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.minTapSize)
                .padding(horizontal = AppTheme.dimens.margin.default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rowTitle,
                style = AppTheme.typography.text.large
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(AppTheme.dimens.margin.smaller))

            rowValue?.let {
                Text(
                    text = it,
                    style = AppTheme.typography.text.large,
                    color = AppTheme.colors.text.tableDetails,
                    textAlign = TextAlign.End
                )
            }
        }

        Divider(color = AppTheme.colors.divider)
    }
}

@Preview(showBackground = true)
@Composable
private fun TextRowPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            TextRow(rowTitle = "Example", rowValue = "Value")
        }
    }
}

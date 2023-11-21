package com.cmtelematics.cmtreferenceapp.theme.ui.component.row

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.TextButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.divider.Divider

@Composable
fun TextButtonRow(
    rowTitle: String,
    buttonTitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .height(AppTheme.dimens.minTapSize)
                .padding(start = AppTheme.dimens.margin.default),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = rowTitle,
                style = AppTheme.typography.text.large
            )

            Spacer(modifier = Modifier.weight(1f))

            TextButton(
                text = buttonTitle,
                onClick = onClick
            )
        }

        Divider(color = AppTheme.colors.divider)
    }
}

@Preview(showBackground = true)
@Composable
private fun TextButtonRowPreview() {
    AppTheme {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(AppTheme.dimens.margin.default)
        ) {
            TextButtonRow(rowTitle = "Example", buttonTitle = "Button", onClick = {})
        }
    }
}

package com.cmtelematics.cmtreferenceapp.crash.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.crash.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun AssistancePointView(
    @DrawableRes iconResource: Int,
    title: String,
    description: String
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(iconResource),
            contentDescription = null,
            alignment = Alignment.TopStart
        )

        Spacer(modifier = Modifier.width(AppTheme.dimens.margin.small))

        Column {
            Text(
                text = title,
                style = AppTheme.typography.title.medium
            )

            Text(
                text = description,
                style = AppTheme.typography.text.medium,
                color = AppTheme.colors.gray.grayDark
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AssistancePointViewPreview() {
    AppTheme {
        AssistancePointView(
            iconResource = R.drawable.ic_crash_list_item_1,
            title = "Title",
            description = "A sample description"
        )
    }
}

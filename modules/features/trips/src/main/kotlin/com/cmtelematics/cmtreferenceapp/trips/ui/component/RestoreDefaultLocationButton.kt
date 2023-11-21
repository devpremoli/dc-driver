package com.cmtelematics.cmtreferenceapp.trips.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.trips.R

@Composable
internal fun RestoreDefaultLocationButton(modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier.size(AppTheme.dimens.minTapSize),
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            backgroundColor = AppTheme.colors.button.contained.content
        )
    ) {
        Image(painter = painterResource(R.drawable.ic_recenter), contentDescription = null)
    }
}

@Preview
@Composable
private fun RestoreDefaultLocationButtonPreview() {
    AppTheme {
        RestoreDefaultLocationButton(
            modifier = Modifier,
            onClick = { }
        )
    }
}

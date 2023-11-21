package com.cmtelematics.cmtreferenceapp.theme.ui.component.image

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.cmtelematics.cmtreferenceapp.theme.R

@Composable
fun ReferenceAppLogo(
    modifier: Modifier = Modifier
) = Image(
    modifier = modifier,
    painter = painterResource(
        id = R.drawable.ic_logo_large
    ),
    contentScale = ContentScale.Fit,
    contentDescription = null
)

@Composable
fun PoweredByLogo(
    modifier: Modifier = Modifier
) = Image(
    modifier = modifier,
    painter = painterResource(id = R.drawable.ic_powered_cmt),
    contentScale = ContentScale.Fit,
    contentDescription = null
)

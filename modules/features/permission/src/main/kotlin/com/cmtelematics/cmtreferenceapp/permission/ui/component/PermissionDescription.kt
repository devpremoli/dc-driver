package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cmtelematics.cmtreferenceapp.theme.ui.component.text.EmphasizedText

@Composable
internal fun PermissionDescription(modifier: Modifier = Modifier, description: String, textAlign: TextAlign? = null) {
    EmphasizedText(modifier = modifier, text = description, textAlign = textAlign)
}

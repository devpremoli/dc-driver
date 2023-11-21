package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.common.model.PermissionType
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton

@Composable
internal fun SingleInstructionPermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit,
    title: String,
    description: String,
    instruction: String
) {
    PermissionRequesterScreenScaffold(
        requiredPermissionType = requiredPermissionType,
        markAsRequested = markAsRequested,
        refreshPermissionStates = refreshPermissionStates
    ) { requestPermission ->

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        Text(
            text = title,
            style = AppTheme.typography.title.xxLarge
        )

        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))

        Column(
            modifier = Modifier
                .padding(horizontal = AppTheme.dimens.margin.default)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PermissionDescription(
                description = description,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

            PermissionInstructions(instruction)
        }

        Spacer(modifier = Modifier.weight(1f))

        PrimaryButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.permission_next_button),
            onClick = { requestPermission() },
            buttonSize = ButtonSize.Large
        )
    }
}

@Preview
@Composable
private fun SingleInstructionPermissionScreenContentPreview() {
    AppTheme {
        SingleInstructionPermissionScreenContent(
            requiredPermissionType = PermissionType.FineLocation,
            markAsRequested = { },
            refreshPermissionStates = { },
            title = "Test title",
            description = "Test description",
            instruction = "Test instruction"
        )
    }
}

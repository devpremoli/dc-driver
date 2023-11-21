package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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
internal fun PermissionScreenContent(
    requiredPermissionType: PermissionType,
    markAsRequested: (PermissionType) -> Unit,
    refreshPermissionStates: () -> Unit,
    title: String,
    description: String,
    instructionSteps: List<String>? = null
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

        Column(Modifier.padding(horizontal = AppTheme.dimens.margin.default)) {
            val hasInstruction = !instructionSteps.isNullOrEmpty()
            PermissionDescription(
                description = description,
                textAlign = if (!hasInstruction) {
                    TextAlign.Center
                } else {
                    null
                }
            )
            if (hasInstruction) {
                PermissionDescription(description = description)

                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

                PermissionInstructions(
                    instructionSteps = instructionSteps ?: error("instructionSteps must be not null.")
                )
            }
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
private fun PermissionScreenContentPreview() {
    AppTheme {
        PermissionScreenContent(
            requiredPermissionType = PermissionType.FineLocation,
            markAsRequested = { },
            refreshPermissionStates = { },
            title = "Permission title",
            description = "Permission description",
            instructionSteps = listOf("Instruction 1", "Instruction 2", "Instruction 3")
        )
    }
}

@Preview
@Composable
private fun PermissionScreenWithoutInstructionsContentPreview() {
    AppTheme {
        PermissionScreenContent(
            requiredPermissionType = PermissionType.FineLocation,
            markAsRequested = { },
            refreshPermissionStates = { },
            title = "Permission title",
            description = "Permission description"
        )
    }
}

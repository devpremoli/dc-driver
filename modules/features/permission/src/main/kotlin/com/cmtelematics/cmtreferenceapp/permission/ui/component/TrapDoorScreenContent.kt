package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.permission.util.TrapDoorBackHandler
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun TrapDoorScreenContent(
    refreshPermissionStates: () -> Unit,
    description: String,
    instructionSteps: List<String>? = null,
    instructionStepsDescription: String = stringResource(id = R.string.trapdoor_steps_description),
    openPermissionSpecificSettings: (() -> Unit)? = null
) {
    TrapDoorBackHandler()

    TrapDoorScreenScaffold(
        refreshPermissionStates = refreshPermissionStates,
        openPermissionSpecificSettings = openPermissionSpecificSettings
    ) {
        Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))

        Text(
            text = stringResource(id = R.string.trapdoor_title),
            style = AppTheme.typography.title.xxLarge,
            textAlign = TextAlign.Center
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
                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

                Text(text = instructionStepsDescription)

                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))

                PermissionInstructions(
                    instructionSteps = instructionSteps ?: error("instructionSteps must be not null.")
                )
            }
        }
    }
}

@Preview
@Composable
private fun TrapDoorScreenContentPreview() {
    AppTheme {
        TrapDoorScreenContent(
            refreshPermissionStates = { },
            description = "Trapdoor description",
            instructionSteps = listOf("Instruction 1", "Instruction 2", "Instruction 3")
        )
    }
}

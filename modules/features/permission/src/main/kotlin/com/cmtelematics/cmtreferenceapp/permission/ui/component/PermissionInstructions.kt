package com.cmtelematics.cmtreferenceapp.permission.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.cmtelematics.cmtreferenceapp.permission.R
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.text.EmphasizedText

@Composable
internal fun PermissionInstructions(instruction: String) {
    EmphasizedText(
        text = instruction,
        textAlign = TextAlign.Center
    )
}

@Composable
internal fun PermissionInstructions(
    modifier: Modifier = Modifier,
    instructionSteps: List<String>,
    withBulletPoints: Boolean = false
) {
    val bulletMargin = AppTheme.dimens.margin.tinier
    val paragraphSpace = AppTheme.dimens.margin.roomy
    ConstraintLayout(
        modifier = modifier.fillMaxWidth()
    ) {
        val rowReferences: List<Pair<ConstrainedLayoutReference, ConstrainedLayoutReference>> = instructionSteps.map {
            val (rowNumber, rowLine) = createRefs()
            rowNumber to rowLine
        }

        instructionSteps.forEachIndexed { rowIndex, stepDescription ->
            val (previousRowNumber, previousRowLine) = if (rowIndex > 0) {
                rowReferences[rowIndex - 1]
            } else {
                null to null
            }
            val (currentRowNumber, currentRowLine) = rowReferences[rowIndex]

            Text(
                text = if (withBulletPoints) {
                    BULLET_POINT
                } else {
                    stringResource(id = R.string.permission_instruction_step_number, rowIndex + 1)
                },
                style = AppTheme.typography.text.large,
                modifier = Modifier.constrainAs(currentRowNumber) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    if (rowIndex == 0) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    } else {
                        start.linkTo(previousRowNumber?.start ?: error("previousRowNumber must be not null"))
                        end.linkTo(previousRowNumber.end)
                        top.linkTo(currentRowLine.top)
                    }
                }
            )

            EmphasizedText(
                text = stepDescription,
                modifier = Modifier.constrainAs(currentRowLine) {
                    if (rowIndex == 0) {
                        top.linkTo(parent.top)
                        start.linkTo(currentRowNumber.end, margin = bulletMargin)
                    } else {
                        top.linkTo(
                            previousRowLine?.bottom ?: error("previousRowNumber must be not null"),
                            margin = paragraphSpace
                        )
                        start.linkTo(previousRowLine.start)
                    }
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }
}

private const val BULLET_POINT = "\u2022"

@Preview(showBackground = true)
@Composable
private fun SinglePermissionInstructionsPreview() {
    AppTheme {
        PermissionInstructions(
            instruction = "Single Instruction"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionInstructionsPreview() {
    AppTheme {
        PermissionInstructions(
            instructionSteps = listOf("Instruction 1", "Instruction 2", "Instruction 3")
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PermissionInstructionsWithBulletPointsPreview() {
    AppTheme {
        PermissionInstructions(
            instructionSteps = listOf("Instruction 1", "Instruction 2", "Instruction 3"),
            withBulletPoints = true
        )
    }
}

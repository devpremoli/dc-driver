package com.cmtelematics.cmtreferenceapp.tags.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.tags.R
import com.cmtelematics.cmtreferenceapp.tags.model.LinkTagViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog.ConfirmDialog
import com.cmtelematics.cmtreferenceapp.theme.ui.component.dialog.ErrorDialog
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

@Composable
internal fun LinkTagScreen(viewModel: LinkTagViewModel = hiltViewModel()) {
    val discoveredTag by viewModel.discoveredTag.collectAsStateInLifecycle()
    val isStartEnabled by viewModel.startEnabled.collectAsStateInLifecycle()
    val noTagFound by viewModel.noTagFound.collectAsStateInLifecycle()

    LinkTagScreenContent(
        navigateBack = { viewModel.navigateBack() },
        startLinking = { viewModel.linkTag() },
        isStartEnabled = isStartEnabled,
        discoveredTag = discoveredTag,
        confirmTagLinking = { viewModel.confirmLinkingTag() },
        cancelTagLinking = { viewModel.cancelLinkingTag() },
        noTagFound = noTagFound,
        dismissNoTagFound = { viewModel.dismissNoTagFound() }
    )
}

@Composable
private fun LinkTagScreenContent(
    navigateBack: () -> Unit,
    startLinking: () -> Unit,
    isStartEnabled: Boolean,
    discoveredTag: String?,
    confirmTagLinking: () -> Unit,
    cancelTagLinking: () -> Unit,
    noTagFound: Boolean,
    dismissNoTagFound: () -> Unit
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                title = stringResource(id = R.string.title_tag_linking),
                action = { ToolbarBackButton(onClick = navigateBack) }
            )
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = AppTheme.dimens.margin.extraLarge)
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))
            Text(text = stringResource(R.string.link_tag_title), style = AppTheme.typography.title.xxLarge)

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))
            LinkingInstructions()

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.link_tag_start_button),
                onClick = startLinking,
                enabled = isStartEnabled,
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraLarge))

            LinkingDialog(
                discoveredTag = discoveredTag,
                cancelTagLinking = cancelTagLinking,
                confirmTagLinking = confirmTagLinking
            )

            if (noTagFound) {
                ErrorDialog(
                    title = stringResource(id = R.string.error_dialog_title_generic),
                    text = stringResource(R.string.error_dialog_no_tag_found_message),
                    dismissButtonText = stringResource(id = R.string.error_dialog_try_again_generic),
                    onDismiss = dismissNoTagFound
                )
            }
        }
    }
}

@Composable
private fun LinkingInstructions() {
    val bulletMargin = AppTheme.dimens.margin.tinier
    val paragraphSpace = AppTheme.dimens.margin.roomy
    ConstraintLayout(
        modifier = Modifier.fillMaxWidth()
    ) {
        val (number1, number2) = createRefs()
        val (line1, line2) = createRefs()

        Text(
            text = "1.",
            style = AppTheme.typography.text.large,
            modifier = Modifier.constrainAs(number1) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
            }
        )
        Text(
            text = "2.",
            style = AppTheme.typography.text.large,
            modifier = Modifier.constrainAs(number2) {
                start.linkTo(number1.start)
                end.linkTo(number1.end)
                top.linkTo(line2.top)
            }
        )
        Text(
            text = stringResource(R.string.link_tag_page_text_line1),
            style = AppTheme.typography.text.large,
            modifier = Modifier.constrainAs(line1) {
                top.linkTo(parent.top)
                start.linkTo(number1.end, margin = bulletMargin)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
        Text(
            text = stringResource(R.string.link_tag_page_text_line2),
            style = AppTheme.typography.text.large,
            modifier = Modifier.constrainAs(line2) {
                top.linkTo(line1.bottom, margin = paragraphSpace)
                start.linkTo(line1.start)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
        )
    }
}

@Composable
private fun LinkingDialog(
    discoveredTag: String?,
    cancelTagLinking: () -> Unit,
    confirmTagLinking: () -> Unit
) {
    val shouldShowDialog by derivedStateOf { discoveredTag != null }
    ConfirmDialog(
        showDialog = shouldShowDialog,
        cancel = cancelTagLinking,
        confirm = confirmTagLinking,
        title = stringResource(R.string.dialog_title_tag_detected),
        text = stringResource(id = R.string.dialog_text_tag_detected, discoveredTag.orEmpty()),
        confirmButtonTitle = stringResource(R.string.dialog_button_confirm),
        cancelButtonTitle = stringResource(R.string.dialog_button_not_my_tag)
    )
}

@Preview
@Composable
private fun LinkTagScreenPreview() {
    AppTheme {
        LinkTagScreenContent(
            navigateBack = {},
            startLinking = {},
            isStartEnabled = true,
            discoveredTag = null,
            confirmTagLinking = {},
            cancelTagLinking = {},
            noTagFound = false,
            dismissNoTagFound = {}
        )
    }
}

@Preview
@Composable
private fun NoTagsFoundPreview() {
    AppTheme {
        LinkTagScreenContent(
            navigateBack = {},
            startLinking = {},
            isStartEnabled = true,
            discoveredTag = null,
            confirmTagLinking = {},
            cancelTagLinking = {},
            noTagFound = true,
            dismissNoTagFound = {}
        )
    }
}

@Preview
@Composable
private fun TagFoundPreview() {
    AppTheme {
        LinkTagScreenContent(
            navigateBack = {},
            startLinking = {},
            isStartEnabled = true,
            discoveredTag = "ab:cd:ef:gh:12:34",
            confirmTagLinking = {},
            cancelTagLinking = {},
            noTagFound = false,
            dismissNoTagFound = {}
        )
    }
}

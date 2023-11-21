package com.cmtelematics.cmtreferenceapp.tags.ui

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.tags.R
import com.cmtelematics.cmtreferenceapp.tags.model.TagLinkedViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

@Composable
internal fun TagLinkedScreen(viewModel: TagLinkedViewModel = hiltViewModel()) {
    TagLinkedScreenContent(
        navigateBack = { viewModel.navigateBack() },
        closeScreen = { viewModel.closeScreen() }
    )
}

@Composable
private fun TagLinkedScreenContent(
    navigateBack: () -> Unit,
    closeScreen: () -> Unit
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
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.margin.extraLarge)
        ) {
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.enormous))
            Text(text = stringResource(R.string.tag_linked_title), style = AppTheme.typography.title.xxLarge)

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))
            Text(text = stringResource(R.string.tag_linked_subtitle), style = AppTheme.typography.text.large)

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                text = stringResource(R.string.tag_linked_done),
                onClick = closeScreen,
                modifier = Modifier.fillMaxWidth(),
                buttonSize = ButtonSize.Large
            )

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.extraLarge))
        }
    }
}

@Preview
@Composable
private fun TagLinkedScreenPreview() {
    AppTheme {
        TagLinkedScreenContent(
            navigateBack = {},
            closeScreen = {}
        )
    }
}

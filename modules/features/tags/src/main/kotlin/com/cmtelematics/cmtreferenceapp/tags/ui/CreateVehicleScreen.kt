package com.cmtelematics.cmtreferenceapp.tags.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.cmtelematics.cmtreferenceapp.common.util.collectAsStateInLifecycle
import com.cmtelematics.cmtreferenceapp.tags.R
import com.cmtelematics.cmtreferenceapp.tags.model.CreateVehicleViewModel
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.theme.ui.component.screen.ScreenScaffold
import com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield.PlainTextField
import com.cmtelematics.cmtreferenceapp.theme.ui.component.textfield.moveFocusToNextView
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.Toolbar
import com.cmtelematics.cmtreferenceapp.theme.ui.component.toolbar.ToolbarBackButton

@Composable
internal fun CreateVehicleScreen(viewModel: CreateVehicleViewModel = hiltViewModel()) {
    val vehicleNickname by viewModel.vehicleNickname.collectAsStateInLifecycle()
    val vehicleMake by viewModel.vehicleMake.collectAsStateInLifecycle()
    val vehicleModel by viewModel.vehicleModel.collectAsStateInLifecycle()
    val isSubmitEnabled by viewModel.isSubmitEnabled.collectAsStateInLifecycle()

    CreateVehicleScreenContent(
        navigateBack = { viewModel.navigateBack() },
        vehicleNickname = vehicleNickname,
        onVehicleNicknameChanged = { viewModel.vehicleNickname.value = it },
        vehicleMake = vehicleMake,
        onVehicleMakeChanged = { viewModel.vehicleMake.value = it },
        vehicleModel = vehicleModel,
        onVehicleModelChanged = { viewModel.vehicleModel.value = it },
        submit = { viewModel.submit() },
        isSubmitEnabled = isSubmitEnabled
    )
}

@Composable
private fun CreateVehicleScreenContent(
    navigateBack: () -> Unit,
    vehicleNickname: String,
    onVehicleNicknameChanged: (String) -> Unit,
    vehicleMake: String,
    onVehicleMakeChanged: (String) -> Unit,
    vehicleModel: String,
    onVehicleModelChanged: (String) -> Unit,
    submit: () -> Unit,
    isSubmitEnabled: Boolean
) {
    ScreenScaffold(
        toolbar = {
            Toolbar(
                title = stringResource(id = R.string.title_toolbar_vehicle_list),
                action = { ToolbarBackButton(onClick = navigateBack) }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.margin.bigger)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))
                Text(
                    text = stringResource(R.string.title_create_a_vehicle),
                    style = AppTheme.typography.title.xxLarge
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.big))
                Text(
                    text = stringResource(R.string.subtitle_create_a_vehicle),
                    style = AppTheme.typography.text.large
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))

                VehicleForm(
                    vehicleNickname = vehicleNickname,
                    onVehicleNicknameChanged = onVehicleNicknameChanged,
                    vehicleMake = vehicleMake,
                    onVehicleMakeChanged = onVehicleMakeChanged,
                    vehicleModel = vehicleModel,
                    onVehicleModelChanged = onVehicleModelChanged,
                    submit = {
                        if (isSubmitEnabled) {
                            submit()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))
            PrimaryButton(
                text = stringResource(R.string.button_next),
                onClick = submit,
                modifier = Modifier.fillMaxWidth(),
                enabled = isSubmitEnabled
            )
            Spacer(modifier = Modifier.height(AppTheme.dimens.margin.bigger))
        }
    }
}

@Composable
private fun VehicleForm(
    vehicleNickname: String,
    onVehicleNicknameChanged: (String) -> Unit,
    vehicleMake: String,
    onVehicleMakeChanged: (String) -> Unit,
    vehicleModel: String,
    onVehicleModelChanged: (String) -> Unit,
    submit: () -> Unit
) {
    val formFocusRequester = remember { FocusRequester() }

    PlainTextField(
        value = vehicleNickname,
        onValueChange = onVehicleNicknameChanged,
        label = "",
        placeholder = stringResource(R.string.field_vehicle_nickname),
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(formFocusRequester),
        keyboardActions = moveFocusToNextView(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.small))
    PlainTextField(
        value = vehicleMake,
        onValueChange = onVehicleMakeChanged,
        label = "",
        placeholder = stringResource(R.string.field_vehicle_make),
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = moveFocusToNextView(),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.small))
    PlainTextField(
        value = vehicleModel,
        onValueChange = onVehicleModelChanged,
        label = "",
        placeholder = stringResource(R.string.field_vehicle_model),
        modifier = Modifier.fillMaxWidth(),
        keyboardActions = KeyboardActions(
            onNext = { submit() }
        ),
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
    )

    LaunchedEffect(Unit) {
        formFocusRequester.requestFocus()
    }
}

@Preview
@Composable
private fun CreateVehicleScreenPreview() {
    AppTheme {
        CreateVehicleScreenContent(
            navigateBack = {},
            vehicleNickname = "",
            onVehicleNicknameChanged = {},
            vehicleMake = "",
            onVehicleMakeChanged = {},
            vehicleModel = "",
            onVehicleModelChanged = {},
            submit = {},
            isSubmitEnabled = true
        )
    }
}

package com.cmtelematics.cmtreferenceapp.trips.ui.component.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.ButtonSize
import com.cmtelematics.cmtreferenceapp.theme.ui.component.button.PrimaryButton
import com.cmtelematics.cmtreferenceapp.trips.R
import com.cmtelematics.sdk.types.UserTransportationMode
import com.cmtelematics.sdk.types.UserTransportationMode.DRIVER
import com.cmtelematics.sdk.types.UserTransportationMode.NOTCAR
import com.cmtelematics.sdk.types.UserTransportationMode.PASSENGER

@Composable
internal fun TransportationModeSelectorDialog(
    showDialog: Boolean,
    cancel: () -> Unit,
    setUserTransportationMode: (UserTransportationMode) -> Unit
) {
    if (showDialog) {
        Dialog(onDismissRequest = { cancel() }) {
            Box(
                modifier = Modifier.background(AppTheme.colors.background.primary),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier.padding(AppTheme.dimens.margin.default),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(id = R.string.transportation_mode_dialog_title),
                        style = AppTheme.typography.title.large,
                        color = AppTheme.colors.secondary
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.tiny))
                    Text(
                        text = stringResource(id = R.string.transportation_mode_dialog_description),
                        style = AppTheme.typography.text.large,
                        color = AppTheme.colors.text.tableDetails
                    )
                    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.default))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf(DRIVER, PASSENGER, NOTCAR).forEach { transportationMode ->
                            TransportationModeOption(
                                modifier = Modifier.clickable { setUserTransportationMode(transportationMode) },
                                transportationMode = transportationMode
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(AppTheme.dimens.margin.large))
                    PrimaryButton(
                        modifier = Modifier.fillMaxWidth(),
                        text = stringResource(id = R.string.transportation_mode_dialog_cancel_button),
                        onClick = { cancel() },
                        buttonSize = ButtonSize.Large
                    )
                }
            }
        }
    }
}

@Composable
private fun TransportationModeOption(modifier: Modifier, transportationMode: UserTransportationMode) {
    Column(
        modifier = modifier.padding(vertical = AppTheme.dimens.margin.extraTiny),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(
                id = when (transportationMode) {
                    DRIVER -> R.drawable.ic_driver_trip_mode
                    PASSENGER -> R.drawable.ic_passenger_trip_mode
                    else -> R.drawable.ic_not_a_car_trip_mode
                }
            ),
            contentDescription = null
        )

        Text(
            text = stringResource(
                id = when (transportationMode) {
                    DRIVER -> R.string.trip_details_driver
                    PASSENGER -> R.string.trip_details_passenger
                    else -> R.string.trip_details_not_a_car
                }
            ),
            style = AppTheme.typography.text.small
        )
    }
}

@Preview
@Composable
private fun TransportationModeSelectorDialogPreview() {
    AppTheme {
        TransportationModeSelectorDialog(
            showDialog = true,
            cancel = { },
            setUserTransportationMode = { }
        )
    }
}

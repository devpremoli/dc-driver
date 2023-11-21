package com.cmtelematics.cmtreferenceapp.ui.component

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.SwipeableDefaults
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.google.accompanist.navigation.material.BottomSheetNavigator

@Composable
fun rememberExpandedBottomSheetNavigator(
    confirmStateChange: (ModalBottomSheetValue) -> Boolean,
    animationSpec: AnimationSpec<Float> = SwipeableDefaults.AnimationSpec
): BottomSheetNavigator {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        animationSpec = animationSpec,
        skipHalfExpanded = true,
        confirmStateChange = confirmStateChange
    )

    return remember(sheetState) {
        BottomSheetNavigator(sheetState = sheetState)
    }
}

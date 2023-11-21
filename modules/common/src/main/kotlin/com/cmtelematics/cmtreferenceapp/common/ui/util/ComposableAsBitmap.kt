package com.cmtelematics.cmtreferenceapp.common.ui.util

import android.graphics.Bitmap
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.graphics.applyCanvas
import androidx.core.view.doOnLayout
import androidx.core.view.isGone

@Composable
internal fun ComposableAsBitmap(
    content: @Composable () -> Unit,
    onCreated: (Bitmap) -> Unit
) {
    val context = LocalContext.current

    val composeView = remember { ComposeView(context) }

    AndroidView(
        factory = {
            composeView.apply {
                doOnLayout { view ->
                    if (view.width <= 0 || view.height <= 0) {
                        return@doOnLayout
                    }

                    val bitmap = createBitmapFromView(
                        view = view,
                        width = view.width,
                        height = view.height
                    )

                    onCreated(bitmap)
                }

                setContent { content() }

                isGone = true
            }
        }
    )
}

private fun createBitmapFromView(view: View, width: Int, height: Int): Bitmap {
    view.layoutParams = LinearLayoutCompat.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    view.measure(
        View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY),
        View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY)
    )

    view.layout(0, 0, width, height)

    return with(view) {
        Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888).applyCanvas {
            draw(this)
        }
    }
}

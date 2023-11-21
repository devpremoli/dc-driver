package com.cmtelematics.cmtreferenceapp.authentication.ui.component

import android.text.Annotation
import android.text.SpannedString
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme

@Composable
internal fun TextWithEmbeddedLink(
    modifier: Modifier,
    text: SpannedString,
    linkAnnotation: String,
    onLinkClick: () -> Unit
) {
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }
    val annotatedText = remember {
        buildAnnotatedText(
            text = text,
            linkAnnotation = linkAnnotation
        )
    }

    Text(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { position ->
                    layoutResult.value?.run {
                        val offset = getOffsetForPosition(position)

                        annotatedText
                            .getStringAnnotations(offset, offset)
                            .firstOrNull { it.tag == LINK_ANNOTATION_TAG }
                            ?.run {
                                if (item == linkAnnotation) {
                                    onLinkClick()
                                }
                            }
                    }
                }
            },
        text = annotatedText,
        style = AppTheme.typography.text.medium,
        color = AppTheme.colors.button.text.content,
        onTextLayout = { layoutResult.value = it }
    )
}

private fun buildAnnotatedText(
    text: SpannedString,
    linkAnnotation: String
): AnnotatedString = buildAnnotatedString {
    append(text.toString())

    addLink(text, linkAnnotation)
}

private fun AnnotatedString.Builder.addLink(text: SpannedString, linkAnnotation: String) {
    val annotations = text.getSpans(0, text.length, Annotation::class.java)

    for (annotation in annotations) {
        if (annotation.key == TYPE_ANNOTATION_KEY) {
            val fontName = annotation.value

            if (fontName == LINK_ANNOTATION_TAG) {
                val start = text.getSpanStart(annotation)
                val end = text.getSpanEnd(annotation)

                addStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold
                    ),
                    start = start,
                    end = end
                )

                addStringAnnotation(
                    tag = LINK_ANNOTATION_TAG,
                    annotation = linkAnnotation,
                    start = start,
                    end = end
                )
            }
        }
    }
}

private const val TYPE_ANNOTATION_KEY = "type"
private const val LINK_ANNOTATION_TAG = "link"

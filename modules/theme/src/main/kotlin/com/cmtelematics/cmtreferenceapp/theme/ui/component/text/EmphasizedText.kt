package com.cmtelematics.cmtreferenceapp.theme.ui.component.text

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme
import com.cmtelematics.cmtreferenceapp.theme.ui.AppTheme.dimens

// https://stackoverflow.com/a/69652081
private val boldRegex = Regex("(?<!\\*)\\*\\*(?!\\*).*?(?<!\\*)\\*\\*(?!\\*)")

@Composable
fun EmphasizedText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    style: TextStyle = AppTheme.typography.text.medium,
    textColor: Color = AppTheme.colors.text.body,
    emphasizedTextStyle: TextStyle? = null
) {
    val boldSections = boldRegex.findAll(text).toList()

    val annotatedString = if (boldSections.isNotEmpty()) {
        buildAnnotatedString {
            // emit leading normal text (if non-zero long)
            (0 until boldSections.first().range.first).takeIf { !it.isEmpty() }?.let { append(text.substring(it)) }

            boldSections
                .zipWithNext()
                .plus(boldSections.last() to null)
                .forEach { (a, b) ->
                    // append the bold text then the normal text following that if not empty
                    append(
                        AnnotatedString(
                            text = a.value.removeSurrounding("**"),
                            spanStyle = emphasizedTextStyle?.mapToSpanStyle() ?: SpanStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = dimens.fontSize.textM,
                                letterSpacing = 0.5.sp,
                                color = AppTheme.colors.text.body
                            )
                        )
                    )

                    val normalTextRange = a.range.last + 1 until (b?.run { range.first } ?: text.length)
                    normalTextRange.takeIf { !it.isEmpty() }?.let { append(text.substring(it)) }
                }
        }
    } else {
        AnnotatedString(text, spanStyles = emptyList())
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        textAlign = textAlign,
        color = textColor,
        style = style
    )
}

private fun TextStyle.mapToSpanStyle() = SpanStyle(
    fontWeight = this.fontWeight,
    fontSize = this.fontSize,
    letterSpacing = this.letterSpacing,
    color = this.color
)

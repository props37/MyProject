package ru.zarina.zarina.util.compose.text

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import ru.zarina.zarina.util.kotlin.findSubstringBounds
import timber.log.Timber

fun AnnotatedString.Builder.addStyles(
    substringToStyles: Map<String, List<SpanStyle>>,
) {
    substringToStyles.forEach { (substring, styles) ->
        val annotatedString = this.toAnnotatedString()
        val substringBounds = annotatedString.findSubstringBounds(substring)
        if (substringBounds != null) {
            styles.forEach { style ->
                addStyle(style, substringBounds.first, substringBounds.last)
            }
        } else {
            Timber.e("Could not add styles because substring \"$substring\" is not found in string \"$annotatedString\"")
        }
    }
}

@OptIn(ExperimentalTextApi::class)
fun AnnotatedString.Builder.addUrlAnnotations(
    substringToStyles: Map<String, List<UrlAnnotation>>,
) {
    substringToStyles.forEach { (substring, annotations) ->
        val annotatedString = this.toAnnotatedString()
        val substringBounds = annotatedString.findSubstringBounds(substring)
        if (substringBounds != null) {
            annotations.forEach { annotation ->
                addUrlAnnotation(annotation, substringBounds.first, substringBounds.last)
            }
        } else {
            Timber.e("Could not add style because substring \"$substring\" is not found in string \"$annotatedString\"")
        }
    }
}

fun AnnotatedString.Builder.addStyle(substring: String, style: SpanStyle) {
    val annotatedString = this.toAnnotatedString()
    val substringBounds = annotatedString.findSubstringBounds(substring)
    if (substringBounds != null) {
        addStyle(style, substringBounds.first, substringBounds.last)
    } else {
        Timber.e("Could not add style because substring $substring is not found in $this")
    }
}

@OptIn(ExperimentalTextApi::class)
fun AnnotatedString.Builder.addUrlAnnotation(substring: String, annotation: UrlAnnotation) {
    val annotatedString = this.toAnnotatedString()
    val substringBounds = annotatedString.findSubstringBounds(substring)
    if (substringBounds != null) {
        addUrlAnnotation(annotation, substringBounds.first, substringBounds.last)
    } else {
        Timber.e("Could not add URL annotation because substring \"$substring\" is not found in string \"$annotatedString\"")
    }
}

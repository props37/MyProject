package ru.livetyping.zarina.util.compose.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.UrlAnnotation
import androidx.compose.ui.text.buildAnnotatedString
import ru.livetyping.zarina.util.kotlin.findSubstringBounds
import timber.log.Timber

@Composable
fun rememberStringWithLinks(
    baseString: String,
    substringToLink: Map<String, LinkAnnotation>,
): AnnotatedString {
    return remember(baseString, substringToLink) {
        buildAnnotatedString {
            append(baseString)
            val string = this.toAnnotatedString()

            substringToLink.forEach { (substring, link) ->
                val substringBounds = string.findSubstringBounds(substring)
                if (substringBounds != null) {
                    when (link) {
                        is LinkAnnotation.Url -> {
                            addLink(link, substringBounds.first, substringBounds.last)
                        }

                        is LinkAnnotation.Clickable -> {
                            addLink(link, substringBounds.first, substringBounds.last)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun rememberStringWithLinks(
    baseString: String,
    substringToUrl: Map<String, String>,
    urlStyle: SpanStyle,
    onUrlClicked: (String) -> Unit,
): AnnotatedString {
    return remember(baseString, substringToUrl) {
        buildAnnotatedString {
            append(baseString)
            val string = this.toAnnotatedString()

            substringToUrl.forEach { (substring, url) ->
                val substringBounds = string.findSubstringBounds(substring)
                if (substringBounds != null) {
                    val urlAnnotation = LinkAnnotation.Url(
                        url = url,
                        style = urlStyle,
                        linkInteractionListener = { link ->
                            if (link is LinkAnnotation.Url) {
                                onUrlClicked(link.url)
                            }
                        }
                    )
                    addLink(urlAnnotation, substringBounds.first, substringBounds.last)
                }
            }
        }
    }
}

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

package ru.livetyping.zarina.util.compose.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import ru.livetyping.zarina.util.kotlin.findSubstringBounds

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
                        styles = TextLinkStyles(urlStyle),
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

fun AnnotatedString.Builder.addStyle(
    substring: String,
    spanStyle: SpanStyle,
) {
    val substringBounds = this.toAnnotatedString().findSubstringBounds(substring)
    if (substringBounds != null) {
        addStyle(spanStyle, substringBounds.first, substringBounds.last)
    }
}

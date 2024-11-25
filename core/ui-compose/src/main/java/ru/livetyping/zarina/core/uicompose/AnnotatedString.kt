package ru.livetyping.zarina.core.uicompose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import ru.livetyping.zarina.core.kotlinutil.findSubstringBounds

@Composable
public fun rememberAnnotatedStringWithLinks(
    baseString: String,
    substringToUrl: Map<String, String>,
    urlStyle: SpanStyle,
    onUrlClicked: (String) -> Unit,
): AnnotatedString {
    return remember(baseString, substringToUrl, urlStyle, onUrlClicked) {
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

package ru.livetyping.zarina.feature.detectedcity.ui.impl.impl.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.buildAnnotatedString
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.detectedcity.ui.impl.R

@Composable
internal fun rememberBodyText(cityName: String): AnnotatedString {
    val bodyTextRaw = stringResource(R.string.detected_city_body, cityName)
    val cityNameStyle = UiKitTheme.typography.secondary.bold
    return remember(bodyTextRaw, cityNameStyle) {
        buildAnnotatedString {
            append(bodyTextRaw)

            val cityNameStartIndex = bodyTextRaw.indexOf(cityName)
            val cityNameBounds = cityNameStartIndex..(cityNameStartIndex + cityName.length)
            val cityNameSpanStyle = cityNameStyle.toSpanStyle()
            addStyle(
                style = cityNameSpanStyle,
                start = cityNameBounds.first,
                end = cityNameBounds.last,
            )
        }
    }
}

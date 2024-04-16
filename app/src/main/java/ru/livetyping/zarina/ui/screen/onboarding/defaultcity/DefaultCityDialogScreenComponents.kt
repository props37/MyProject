package ru.livetyping.zarina.ui.screen.onboarding.defaultcity

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.ui.theme.UiKitTheme

object DefaultCityDialogScreenComponents {

    @Composable
    fun bodyText(defaultCity: City): AnnotatedString {
        val defaultCityName = defaultCity.name
        val bodyTextRaw = stringResource(R.string.default_city_dialog_body, defaultCityName)
        val defaultCityNameStyle = UiKitTheme.typography.secondary.bold
        return remember(bodyTextRaw, defaultCityName, defaultCityNameStyle) {
            buildAnnotatedString {
                append(bodyTextRaw)

                val defaultCityNameStartIndex = bodyTextRaw.indexOf(defaultCityName)
                val defaultCityNameBounds =
                    defaultCityNameStartIndex..(defaultCityNameStartIndex + defaultCityName.length)
                val defaultCityNameSpanStyle = SpanStyle(
                    fontWeight = defaultCityNameStyle.fontWeight,
                    fontStyle = defaultCityNameStyle.fontStyle,
                )
                addStyle(
                    style = defaultCityNameSpanStyle,
                    start = defaultCityNameBounds.first,
                    end = defaultCityNameBounds.last,
                )
            }
        }
    }
}

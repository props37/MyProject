package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.button.CloseButton
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme

object CitySelectorScreenComponents {

    @Composable
    fun TopBar(
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(vertical = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.city),
                style = UiKitTheme.typographyReworked.primary.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.align(Alignment.Center),
            )

            CloseButton(
                onClick = onCloseClicked,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 8.dp),
            )
        }
    }

    @Composable
    fun City(
        city: City,
        onClick: (City) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onClick(city) }
                    .padding(horizontal = 16.dp),
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = city.name,
                    style = UiKitTheme.typographyReworked.secondary.light,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )

                // TODO: [High] Add full name

                Spacer(modifier = Modifier.height(12.dp))
            }

            Divider(
                color = UiKitTheme.colorsReworked.border.general.default,
                thickness = 1.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
            )
        }
    }

    @Composable
    fun CityFirstLetterHeader(
        letter: Char,
        modifier: Modifier = Modifier,
    ) {
        Text(
            text = letter.toString(),
            style = UiKitTheme.typographyReworked.primary.bold,
            color = UiKitTheme.colorsReworked.text.general.regular.default,
            modifier = modifier.padding(start = 16.dp, top = 20.dp, bottom = 4.dp),
        )
    }
}

// TODO: [High] Add PreviewParameterProvider
@Preview
@Composable
private fun CityPreview() {
    ZarinaTheme {
        CitySelectorScreenComponents.City(
            city = City.SAINT_PETERSBURG,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun CityFirstLetterHeaderPreview() {
    ZarinaTheme {
        Column {
            CitySelectorScreenComponents.CityFirstLetterHeader(letter = 'С')
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
            )
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
            )
        }
    }
}

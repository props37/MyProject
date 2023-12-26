package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.ZarinaCircularLoader
import ru.zarina.zarina.ui.common.component.button.CloseButton
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme
import ru.zarina.zarina.util.compose.navigationBarsOrIme
import ru.zarina.zarina.utils.compose.plus

// TODO: [High] Add previews

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
    fun CityList(
        listState: CityListState,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = listState,
            label = "CityList",
            modifier = modifier,
        ) { listState ->
            when (listState) {
                CityListState.InitialLoading -> {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .windowInsetsPadding(WindowInsets.navigationBarsOrIme),
                    ) {
                        ZarinaCircularLoader(
                            color = UiKitTheme.colorsReworked.icon.regular.default,
                            modifier = Modifier.size(40.dp),
                        )
                    }
                }

                is CityListState.CityList -> {
                    if (listState.list.isNotEmpty()) {
                        val baseContentPadding = remember { PaddingValues(top = 8.dp) }
                        val contentPadding =
                            baseContentPadding + WindowInsets.navigationBarsOrIme.asPaddingValues()

                        LazyColumn(
                            contentPadding = contentPadding,
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            items(
                                items = listState.list,
                                key = { getCityListItemKey(it) },
                                contentType = { getCityListItemContentType(it) },
                            ) { item ->
                                when (item) {
                                    is CityListItem.City -> {
                                        City(
                                            city = item.city,
                                            onClick = {},
                                            showFullName = item.showFullName,
                                            modifier = Modifier.fillMaxWidth(),
                                        )
                                    }

                                    is CityListItem.CityFirstLetterHeader -> {
                                        CityFirstLetterHeader(item.letter)
                                    }
                                }
                            }
                        }
                    } else {
                        CityNotFound(
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(WindowInsets.navigationBarsOrIme)
                                .padding(horizontal = 24.dp),
                        )
                    }
                }

                is CityListState.Error -> {
                    // TODO: [High] Implement
                    // TODO: [High] Add nav bar and IME padding
                }
            }
        }
    }

    @Composable
    fun City(
        city: City,
        onClick: (City) -> Unit,
        showFullName: Boolean,
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

                if (showFullName) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = city.fullName,
                        style = UiKitTheme.typographyReworked.footnote.light,
                        color = UiKitTheme.colorsReworked.text.general.regular.muted,
                    )
                }

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
    
    @Composable
    private fun CityNotFound(
        modifier: Modifier = Modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            Text(
                text = stringResource(R.string.city_not_found),
                style = UiKitTheme.typographyReworked.primary.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.try_to_change_city_name),
                style = UiKitTheme.typographyReworked.secondary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
            )
        }
    }

    @Stable
    private fun getCityListItemKey(item: CityListItem): String = when (item) {
        is CityListItem.City -> "$CityListItemCityKeyPrefix ${item.city.kladrId.value}"
        is CityListItem.CityFirstLetterHeader -> {
            "$CityListItemCityFirstLetterHeaderKeyPrefix ${item.letter}"
        }
    }

    @Stable
    private fun getCityListItemContentType(item: CityListItem): String = when (item) {
        is CityListItem.City -> {
            if (!item.showFullName) {
                CityListItemCityContentType
            } else {
                CityListItemCityWithFullNameContentType
            }
        }

        is CityListItem.CityFirstLetterHeader -> CityListItemCityFirstLetterHeaderContentType
    }

    private const val CityListItemCityKeyPrefix = "City"
    private const val CityListItemCityFirstLetterHeaderKeyPrefix = "CityFirstLetterHeader"

    private const val CityListItemCityContentType = "CityContentType"
    private const val CityListItemCityWithFullNameContentType = "CityWithFullNameContentType"
    private const val CityListItemCityFirstLetterHeaderContentType =
        "CityFirstLetterHeaderContentType"
}

// TODO: [High] Add PreviewParameterProvider
@Preview
@Composable
private fun CityPreview() {
    ZarinaTheme {
        Column {
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
                showFullName = true,
            )
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
                showFullName = false,
            )
        }
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
                showFullName = false,
            )
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
                showFullName = true,
            )
        }
    }
}

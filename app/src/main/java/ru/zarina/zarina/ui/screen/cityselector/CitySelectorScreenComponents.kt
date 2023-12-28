package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.geography.City
import ru.zarina.zarina.ui.common.component.IconButtonCustom
import ru.zarina.zarina.ui.common.component.ZarinaCircularLoader
import ru.zarina.zarina.ui.common.component.button.CloseButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.rework.ZarinaTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.Crossfade
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun CitySearchBar(
        cityNameQuery: String,
        onCityNameQueryChanged: (String) -> Unit,
        onClearClicked: () -> Unit,
        onCancelClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val focusState = remember { mutableStateOf<FocusState?>(null) }

        ZarinaTextField(
            value = cityNameQuery,
            onValueChanged = onCityNameQueryChanged,
            placeholder = {
                Text(text = stringResource(R.string.search_cities))
            },
            leadingContent = {
                Icon(
                    painter = painterResource(R.drawable.ic_search_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            innerTrailingContent = {
                CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                    AnimatedVisibility(
                        visible = cityNameQuery.isNotEmpty(),
                        enter = remember { AnimatedContentDefaultEnterTransition },
                        exit = remember { AnimatedContentDefaultExitTransition },
                    ) {
                        IconButtonCustom(
                            onClick = onClearClicked,
                            indication = rememberRipple(bounded = false, radius = 8.dp),
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_clear_new_24),
                                contentDescription = stringResource(R.string.clear),
                                tint = Color.Unspecified,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }
            },
            outerTrailingContent = {
                // TODO: [High] Migrate to cell ZarinaButton
                val isCancelButtonVisible = focusState.value?.isFocused == true
                AnimatedContent(
                    targetState = isCancelButtonVisible,
                    transitionSpec = {
                        AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                    },
                    contentAlignment = Alignment.Center,
                    label = "CitySearchBar Cancel button",
                ) { isVisible ->
                    if (isVisible) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .heightIn(min = 40.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .clickable(onClick = onCancelClicked)
                                .padding(horizontal = 8.dp, vertical = 8.dp),
                        ) {
                            Text(text = stringResource(R.string.cancel).uppercase())
                        }
                    }
                }
            },
            singleLine = true,
            modifier = modifier.onFocusChanged { focusState.value = it },
        )
    }

    @Composable
    fun CityList(
        listState: CityListState,
        selectedCity: City?,
        onCityClicked: (City) -> Unit,
        isChangeCityButtonVisible: Boolean,
        onChangeCityClicked: () -> Unit,
        onErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier.fillMaxSize()) {
            Crossfade(
                targetState = listState,
                contentKey = { getCityListContentKey(it) },
                label = "CityList",
            ) { listState ->
                when (listState) {
                    CityListState.Loading -> {
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
                            val baseContentPadding = remember(isChangeCityButtonVisible) {
                                val bottom = if (isChangeCityButtonVisible) {
                                    val buttonHeight = ZarinaButtonDefaults.HeightLarge
                                    buttonHeight + ConfirmButtonBottomPadding + 8.dp
                                } else {
                                    0.dp
                                }
                                PaddingValues(top = 8.dp, bottom = bottom)
                            }
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
                                                onClick = onCityClicked,
                                                showFullName = item.showFullName,
                                                isSelected = item.city.kladrId == selectedCity?.kladrId,
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
                        CitySearchError(
                            errorType = listState.type,
                            onRefreshClicked = onErrorRefreshClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .windowInsetsPadding(WindowInsets.navigationBarsOrIme),
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = isChangeCityButtonVisible,
                enter = remember { AnimatedContentDefaultEnterTransition },
                exit = remember { AnimatedContentDefaultExitTransition },
                modifier = Modifier.align(Alignment.BottomCenter),
            ) {
                ZarinaButton(
                    onClick = onChangeCityClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBarsOrIme)
                        .padding(horizontal = 16.dp)
                        .padding(bottom = 20.dp),
                ) {
                    Text(text = stringResource(R.string.change).uppercase())
                }
            }
        }
    }

    @Composable
    fun City(
        city: City,
        onClick: (City) -> Unit,
        showFullName: Boolean,
        isSelected: Boolean,
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

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
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
                    }

                    CityCheckmark(
                        isVisible = isSelected,
                        modifier = Modifier.padding(start = if (isSelected) 16.dp else 0.dp),
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

    // TODO: [Low] Write custom animation
    @Composable
    private fun CityCheckmark(
        isVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            Icon(
                painter = painterResource(R.drawable.ic_check_24),
                contentDescription = stringResource(R.string.checked),
                tint = UiKitTheme.colorsReworked.icon.regular.default,
                modifier = Modifier.size(20.dp),
            )

            val maskWidthFraction = animateFloatAsState(
                targetValue = if (isVisible) 0f else 1f,
                animationSpec = tween(durationMillis = 200),
                label = "CityCheckmark",
            )
            val maskColor = UiKitTheme.colorsReworked.background.general.regular.background

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .drawBehind {
                        val width = size.width * maskWidthFraction.value
                        val topLeft = Offset(size.width - width, 0f)
                        val size = Size(width, size.height)
                        drawRect(
                            color = maskColor,
                            topLeft = topLeft,
                            size = size,
                        )
                    }
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

    @Composable
    private fun CitySearchError(
        errorType: CityListState.Error.Type,
        onRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val iconResId: Int
        val titleResId: Int
        val bodyResId: Int
        when (errorType) {
            CityListState.Error.Type.NETWORK -> {
                iconResId = R.drawable.ic_wifi_error_24
                titleResId = R.string.connection_error_title
                bodyResId = R.string.connection_error_body
            }
            CityListState.Error.Type.OTHER -> {
                iconResId = R.drawable.ic_heart_broken_24
                titleResId = R.string.something_went_wrong
                bodyResId = R.string.refresh_page_or_come_back_later
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier,
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Icon(
                painter = painterResource(iconResId),
                contentDescription = null,
                tint = UiKitTheme.colorsReworked.icon.regular.disabled,
                modifier = Modifier.size(64.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(titleResId),
                style = UiKitTheme.typographyReworked.primary.bold,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(bodyResId),
                style = UiKitTheme.typographyReworked.secondary.regular,
                color = UiKitTheme.colorsReworked.text.general.regular.default,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp),
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(16.dp))

            ZarinaButton(
                onClick = onRefreshClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 20.dp),
            ) {
                Text(text = stringResource(R.string.refresh).uppercase())
            }
        }
    }

    @Stable
    private fun getCityListContentKey(state: CityListState): String {
        return when (state) {
            CityListState.Loading -> CityListContentKeyLoading
            is CityListState.CityList -> {
                if (state.list.isNotEmpty()) CityListContentKeyCities else CityListContentKeyCityNotFound
            }

            is CityListState.Error -> CityListContentKeyError
        }
    }

    @Stable
    private fun getCityListItemKey(item: CityListItem): String = when (item) {
        is CityListItem.City -> "$CityListItemKeyPrefixCity ${item.city.kladrId.value}"
        is CityListItem.CityFirstLetterHeader -> {
            "$CityListItemKeyPrefixCityFirstLetterHeader ${item.letter}"
        }
    }

    @Stable
    private fun getCityListItemContentType(item: CityListItem): String = when (item) {
        is CityListItem.City -> {
            if (!item.showFullName) {
                CityListItemContentTypeCity
            } else {
                CityListItemContentTypeCityWithFullName
            }
        }

        is CityListItem.CityFirstLetterHeader -> CityListItemContentTypeCityFirstLetterHeader
    }

    private val ConfirmButtonBottomPadding = 20.dp

    private const val CityListContentKeyLoading = "CityListContentKeyLoading"
    private const val CityListContentKeyCities = "CityListContentKeyCities"
    private const val CityListContentKeyCityNotFound = "CityListContentKeyCityNotFound"
    private const val CityListContentKeyError = "CityListContentKeyError"

    private const val CityListItemKeyPrefixCity = "CityListItemKeyPrefixCity"
    private const val CityListItemKeyPrefixCityFirstLetterHeader =
        "CityListItemKeyPrefixCityFirstLetterHeader"

    private const val CityListItemContentTypeCity = "CityListItemContentTypeCity"
    private const val CityListItemContentTypeCityWithFullName =
        "CityListItemContentTypeCityWithFullName"
    private const val CityListItemContentTypeCityFirstLetterHeader =
        "CityListItemContentTypeCityFirstLetterHeader"
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
                isSelected = false,
            )
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
                showFullName = false,
                isSelected = true,
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
                isSelected = false,
            )
            CitySelectorScreenComponents.City(
                city = City.SAINT_PETERSBURG,
                onClick = {},
                showFullName = true,
                isSelected = true,
            )
        }
    }
}

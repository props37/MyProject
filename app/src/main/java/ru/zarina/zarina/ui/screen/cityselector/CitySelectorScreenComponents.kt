package ru.zarina.zarina.ui.screen.cityselector

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.base.text.textString
import ru.zarina.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.icon.ZarinaCheckmarkAnimatedIcon
import ru.zarina.zarina.ui.common.component.loader.ZarinaCircularLoader
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.zarina.zarina.ui.screen.cityselector.CitySelectorViewModel.CityListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.animation.Crossfade
import ru.zarina.zarina.util.compose.navigationBarsOrIme
import ru.zarina.zarina.utils.compose.plus

object CitySelectorScreenComponents {

    @Composable
    fun TopBar(
        title: Text,
        onBackClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = textString(title),
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

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
                    painter = painterResource(R.drawable.ic_magnifying_glass_24),
                    contentDescription = null,
                    modifier = Modifier.size(20.dp),
                )
            },
            innerTrailingContent = {
                ZarinaTextFieldDefaults.ClearButton(
                    isVisible = cityNameQuery.isNotEmpty(),
                    onClick = onClearClicked,
                )
            },
            outerTrailingContent = {
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
                        ZarinaTextFieldDefaults.CancelButton(onClick = onCancelClicked)
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
                                color = UiKitTheme.colors.icon.regular.default,
                                modifier = Modifier.size(40.dp),
                            )
                        }
                    }

                    is CityListState.CityList -> {
                        if (listState.items.isNotEmpty()) {
                            val baseContentPadding = remember(isChangeCityButtonVisible) {
                                val bottom = if (isChangeCityButtonVisible) {
                                    val buttonHeight = ZarinaButtonDefaults.SizeLarge
                                    buttonHeight + ChangeCityButtonBottomPadding + 8.dp
                                } else {
                                    0.dp
                                }
                                PaddingValues(top = 8.dp, bottom = bottom)
                            }
                            val contentPadding =
                                baseContentPadding +
                                        WindowInsets.safeDrawing
                                            .only(WindowInsetsSides.Bottom)
                                            .asPaddingValues()

                            LazyColumn(
                                contentPadding = contentPadding,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                items(
                                    items = listState.items,
                                    key = { getCityListItemKey(it) },
                                    contentType = { getCityListItemContentType(it) },
                                ) { item ->
                                    when (item) {
                                        is CityListItem.CityItem -> {
                                            City(
                                                city = item.city,
                                                onClick = onCityClicked,
                                                showFullName = item.showFullName,
                                                isSelected = item.city.kladrId == selectedCity?.kladrId,
                                                modifier = Modifier.fillMaxWidth(),
                                            )
                                        }

                                        is CityListItem.CityFirstLetterHeaderItem -> {
                                            CityFirstLetterHeader(item.letter)
                                        }
                                    }
                                }
                            }
                        } else {
                            // TODO: [Low] Implement as CityListState.Error?
                            CityNotFound(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .windowInsetsPadding(WindowInsets.navigationBarsOrIme)
                                    .padding(horizontal = 24.dp),
                            )
                        }
                    }

                    is CityListState.Error -> {
                        ZarinaErrorScreen(
                            state = listState.errorState,
                            onButtonClicked = onErrorRefreshClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 16.dp)
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
                        .padding(bottom = ChangeCityButtonBottomPadding),
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
                            style = UiKitTheme.typography.secondary.light,
                            color = UiKitTheme.colors.text.general.regular.default,
                        )

                        if (showFullName) {
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = city.fullName,
                                style = UiKitTheme.typography.footnote.light,
                                color = UiKitTheme.colors.text.general.regular.muted,
                            )
                        }
                    }

                    ZarinaCheckmarkAnimatedIcon(
                        isVisible = isSelected,
                        iconSize = 16.dp,
                        modifier = Modifier.padding(start = if (isSelected) 16.dp else 0.dp),
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Divider(
                color = UiKitTheme.colors.border.general.default,
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
            style = UiKitTheme.typography.primary.bold,
            color = UiKitTheme.colors.text.general.regular.default,
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
                style = UiKitTheme.typography.primary.bold,
                color = UiKitTheme.colors.text.general.regular.default,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.try_to_change_city_name),
                style = UiKitTheme.typography.secondary.regular,
                color = UiKitTheme.colors.text.general.regular.default,
                textAlign = TextAlign.Center,
            )
        }
    }

    private fun getCityListContentKey(state: CityListState): Any {
        return when (state) {
            is CityListState.CityList -> {
                if (state.items.isNotEmpty()) CityListContentKeyCities else CityListContentKeyCityNotFound
            }
            CityListState.Loading, is CityListState.Error -> state
        }
    }

    @Stable
    private fun getCityListItemKey(item: CityListItem): String = when (item) {
        is CityListItem.CityItem -> "$CityListItemKeyPrefixCity ${item.city.kladrId.value}"
        is CityListItem.CityFirstLetterHeaderItem -> {
            "$CityListItemKeyPrefixCityFirstLetterHeader ${item.letter}"
        }
    }

    @Stable
    private fun getCityListItemContentType(item: CityListItem): String = when (item) {
        is CityListItem.CityItem -> {
            if (!item.showFullName) {
                CityListItemContentTypeCity
            } else {
                CityListItemContentTypeCityWithFullName
            }
        }

        is CityListItem.CityFirstLetterHeaderItem -> CityListItemContentTypeCityFirstLetterHeader
    }

    private val ChangeCityButtonBottomPadding = 16.dp

    private const val CityListContentKeyCities = "CityListContentKeyCities"
    private const val CityListContentKeyCityNotFound = "CityListContentKeyCityNotFound"

    private const val CityListItemKeyPrefixCity = "CityListItemKeyPrefixCity"
    private const val CityListItemKeyPrefixCityFirstLetterHeader =
        "CityListItemKeyPrefixCityFirstLetterHeader"

    private const val CityListItemContentTypeCity = "CityListItemContentTypeCity"
    private const val CityListItemContentTypeCityWithFullName =
        "CityListItemContentTypeCityWithFullName"
    private const val CityListItemContentTypeCityFirstLetterHeader =
        "CityListItemContentTypeCityFirstLetterHeader"
}

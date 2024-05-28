package ru.livetyping.zarina.presentation.screen.cityselector

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.itemsIndexed
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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.icon.ZarinaCheckmarkAnimatedIcon
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.CityListItem
import ru.livetyping.zarina.presentation.screen.cityselector.CitySelectorViewModel.CityListState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.navigationBarsOrIme
import ru.livetyping.zarina.util.compose.plus

@Suppress("ConstPropertyName")
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
                            ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                        }
                    }

                    is CityListState.CityList -> {
                        if (listState.items.isNotEmpty()) {
                            val baseContentPadding = remember(isChangeCityButtonVisible) {
                                val bottomBase = 24.dp
                                val bottom = if (isChangeCityButtonVisible) {
                                    val buttonHeight = ZarinaButtonDefaults.SizeLarge
                                    buttonHeight + ChangeCityButtonBottomPadding + 8.dp
                                } else {
                                    0.dp
                                }
                                PaddingValues(top = 8.dp, bottom = bottom + bottomBase)
                            }
                            val contentPadding = WindowInsets.safeDrawing
                                .only(WindowInsetsSides.Bottom)
                                .asPaddingValues()
                                .plus(baseContentPadding)

                            LazyColumn(
                                contentPadding = contentPadding,
                                modifier = Modifier.fillMaxWidth(),
                            ) {
                                itemsIndexed(
                                    items = listState.items,
                                    key = { _, item -> getCityListItemKey(item) },
                                    contentType = { _, item -> getCityListItemContentType(item) },
                                ) { index, item ->
                                    when (item) {
                                        is CityListItem.CityItem -> {
                                            Column(modifier = Modifier.animateItem()) {
                                                City(
                                                    city = item.city,
                                                    onClick = onCityClicked,
                                                    showFullName = item.showFullName,
                                                    isSelected = item.city.kladrId == selectedCity?.kladrId,
                                                    modifier = Modifier.fillMaxWidth(),
                                                )

                                                if (index < listState.items.lastIndex) {
                                                    ZarinaDivider(
                                                        modifier = Modifier
                                                            .fillMaxWidth()
                                                            .padding(horizontal = 16.dp),
                                                    )
                                                }
                                            }
                                        }

                                        is CityListItem.CityFirstLetterHeaderItem -> {
                                            CityFirstLetterHeader(
                                                letter = item.letter,
                                                modifier = Modifier.animateItem(),
                                            )
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
        ZarinaItem(
            onClick = { onClick(city) },
            startContent = {
                Column {
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
            },
            endContent = {
                ZarinaCheckmarkAnimatedIcon(
                    isVisible = isSelected,
                    iconSize = 16.dp,
                    modifier = Modifier.padding(start = if (isSelected) 16.dp else 0.dp),
                )
            },
            modifier = modifier,
        )
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

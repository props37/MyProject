package ru.zarina.zarina.ui.screens.bases.selectcity

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.collections.immutable.ImmutableList
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.old.City
import ru.zarina.zarina.ui.common.base.ErrorStateOld
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.components.ModalError
import ru.zarina.zarina.ui.common.components.StateSnackbar
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.ui.CityListItemProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.old.ZarinaTheme
import ru.zarina.zarina.utils.compose.navigationOrIme

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun SelectCityScreenContent(
    isSearchLoadingVisible: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    cityItems: ImmutableList<SelectCityComponent.CityListItem>,
    onCityClick: (City) -> Unit,
    isRegionVisible: Boolean,
    errorType: SelectCityComponent.ErrorType?,
    onErrorButtonClick: (SelectCityComponent.ErrorType) -> Unit,
    onCloseClick: () -> Unit,
    isSnackbarVisible: Boolean,
    snackbarText: Text,
    isApplyButtonVisible: Boolean = false,
    onApplyButtonClick: () -> Unit = {},
    /** Whether the automatic scroll to top when [cityItems] are changed is enabled */
    isAutoscrollEnabled: Boolean = true,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colorsOld.screenBackground),
    ) {
        ScreenToolbar(
            title = stringResource(id = R.string.city),
            endIcon = {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_close_24),
                        contentDescription = stringResource(id = R.string.skip),
                    )
                }
            }
        )
        val lazyListState = rememberLazyListState()
        LaunchedEffect(isAutoscrollEnabled, cityItems) {
            if (isAutoscrollEnabled) {
                lazyListState.scrollToItem(
                    3.coerceAtMost(cityItems.lastIndex)
                        .coerceAtMost(lazyListState.firstVisibleItemIndex)
                        .coerceAtLeast(0)
                )
                lazyListState.animateScrollToItem(0)
            }
        }
        val isElevated by remember { derivedStateOf { lazyListState.canScrollBackward } }
        val elevation by animateDpAsState(
            if (isElevated) 6.dp else 0.dp,
            label = "search bar elevation"
        )
        SearchBar(
            isSearchLoadingVisible = isSearchLoadingVisible,
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .shadow(elevation = elevation)
                .zIndex(1f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            AnimatedContent(
                targetState = errorType,
                transitionSpec = { fadeIn() with fadeOut() },
                label = "error state",
                modifier = Modifier.fillMaxSize()
            ) { type ->
                if (type != null) {
                    val state = when (type) {
                        SelectCityComponent.ErrorType.NETWORK -> ErrorStateOld.NETWORK
                        SelectCityComponent.ErrorType.NO_RESULTS -> ErrorStateOld(
                            subtitle = Text.Resource(R.string.city_not_found),
                        )

                        SelectCityComponent.ErrorType.GENERIC -> ErrorStateOld.GENERIC
                    }
                    ModalError(
                        state = state,
                        onButtonClick = { onErrorButtonClick(type) },
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.navigationOrIme.asPaddingValues())
                    )
                } else {
                    LazyColumn(
                        state = lazyListState,
                        contentPadding = WindowInsets.navigationOrIme.asPaddingValues(),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        cityItems.forEach { item ->
                            when (item) {
                                is SelectCityComponent.CityListItem.Header -> item(
                                    key = item.key,
                                    contentType = item.contentType,
                                ) {
                                    CityHeader(
                                        text = item.letter,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }

                                is SelectCityComponent.CityListItem.Item -> item(
                                    key = item.key,
                                    contentType = item.contentType,
                                ) {
                                    val cityModifier = Modifier
                                        .fillMaxWidth()
                                        .background(UiKitTheme.colorsOld.screenBackground)
                                        .clickable(onClick = { onCityClick(item.city) })
                                        .animateItemPlacement()
                                    if (isRegionVisible)
                                        CityExtendedItem(
                                            city = item.city,
                                            isSelected = item.isSelected,
                                            modifier = cityModifier,
                                        )
                                    else
                                        CitySimpleItem(
                                            city = item.city,
                                            isSelected = item.isSelected,
                                            modifier = cityModifier,
                                        )
                                }
                            }
                        }
                    }
                }
            }
            StateSnackbar(
                isVisible = isSnackbarVisible,
                text = snackbarText,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .systemBarsPadding()
            )
        }
        AnimatedContent(
            targetState = isApplyButtonVisible,
            transitionSpec = { fadeIn() with fadeOut() },
            label = "apply button",
            modifier = Modifier.fillMaxWidth()
        ) {
            if (it) {
                val elevation = if (lazyListState.canScrollForward) 6.dp else 0.dp
                ZarinaTextButton(
                    text = stringResource(id = R.string.apply),
                    onClick = onApplyButtonClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(elevation)
                        .background(UiKitTheme.colorsOld.screenBackground)
                        .padding(16.dp)
                        .padding(WindowInsets.navigationOrIme.asPaddingValues())
                )
            }
        }
    }
}

@Composable
private fun CityHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Divider(
        color = UiKitTheme.colorsOld.listDivider,
        thickness = Dp.Hairline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Text(
        text = text,
        style = UiKitTheme.typographyOld.circle2026,
        color = UiKitTheme.colorsOld.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = modifier
            .background(UiKitTheme.colorsOld.screenBackground)
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CitySimpleItem(
    city: City,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Divider(
        color = UiKitTheme.colorsOld.listDivider,
        thickness = Dp.Hairline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(UiKitTheme.colorsOld.screenBackground)
            .padding(16.dp)
    ) {
        Text(
            text = city.name,
            style = UiKitTheme.typographyOld.circle1618,
            color = UiKitTheme.colorsOld.primaryContentColor,
            textAlign = TextAlign.Start,
            modifier = Modifier.weight(1f),
        )
        AnimatedContent(
            targetState = isSelected,
            label = "is ${city.name} selected",
        ) { isSelected ->
            if (isSelected)
                Icon(
                    painter = painterResource(id = R.drawable.ic_checkmark_24),
                    contentDescription = stringResource(id = R.string.selected),
                    tint = UiKitTheme.colorsOld.primaryContentColor,
                    modifier = Modifier.size(20.dp),
                )
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun CityExtendedItem(
    city: City,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Divider(
        color = UiKitTheme.colorsOld.listDivider,
        thickness = Dp.Hairline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(UiKitTheme.colorsOld.screenBackground)
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = city.name,
                style = UiKitTheme.typographyOld.circle1618,
                color = UiKitTheme.colorsOld.primaryContentColor,
                textAlign = TextAlign.Start,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = city.region.orEmpty(),
                style = UiKitTheme.typographyOld.circle1316,
                color = UiKitTheme.colorsOld.listItemSubtitle,
                textAlign = TextAlign.Start,
            )
        }
        AnimatedContent(
            targetState = isSelected,
            label = "is ${city.name} selected",
        ) { isSelected ->
            if (isSelected)
                Icon(
                    painter = painterResource(id = R.drawable.ic_checkmark_24),
                    contentDescription = stringResource(id = R.string.selected),
                    tint = UiKitTheme.colorsOld.primaryContentColor,
                )
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun SearchBar(
    isSearchLoadingVisible: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .background(UiKitTheme.colorsOld.screenBackground)
            .padding(horizontal = 16.dp),
    ) {
        AnimatedContent(
            targetState = isSearchLoadingVisible,
            label = "search loader visibility"
        ) { isLoadingVisible ->
            if (isLoadingVisible)
                CircularProgressIndicator(
                    color = UiKitTheme.colorsOld.primaryContentColor,
                    strokeWidth = 2.dp,
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp),
                )
            else
                Icon(
                    painter = painterResource(R.drawable.ic_search_24),
                    contentDescription = null,
                )
        }

        Spacer(modifier = Modifier.width(12.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 12.dp)
        ) {
            androidx.compose.animation.AnimatedVisibility(
                visible = query.isEmpty(),
                enter = fadeIn(),
                exit = ExitTransition.None,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.city_search),
                    style = UiKitTheme.typographyOld.circle1518,
                    color = UiKitTheme.colorsOld.hint,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = { onQueryChange(it) },
                textStyle = UiKitTheme.typographyOld.circle1518.copy(color = UiKitTheme.colorsOld.primaryContentColor),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        AnimatedVisibility(
            visible = query.isNotEmpty(),
        ) {
            Image(
                painter = painterResource(R.drawable.ic_clear_24),
                contentDescription = stringResource(R.string.clear),
                modifier = Modifier
                    .padding(start = 12.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { onQueryChange("") }
                    ),
            )
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun SelectCityScreenContentPreview(
    @PreviewParameter(CityListItemProvider::class, limit = 1)
    cityItems: ImmutableList<SelectCityComponent.CityListItem>,
) {
    ZarinaTheme {
        SelectCityScreenContent(
            isSearchLoadingVisible = true,
            query = "",
            onQueryChange = {},
            errorType = null,
            cityItems = cityItems,
            onCityClick = {},
            isRegionVisible = true,
            onErrorButtonClick = {},
            onCloseClick = {},
            isSnackbarVisible = false,
            snackbarText = Text.Empty,
        )
    }
}

package ru.zarina.zarina.ui.screens.cityselection

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.components.ModalError
import ru.zarina.zarina.ui.common.components.StateSnackbar
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.ui.CityListItemProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme
import ru.zarina.zarina.utils.compose.navigationOrIme

@OptIn(
    ExperimentalFoundationApi::class,
    ExperimentalAnimationApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun CitySelectionScreenContent(
    isSearchLoadingVisible: Boolean,
    query: String,
    onQueryChange: (String) -> Unit,
    cityItems: ImmutableList<CitySelectionViewModel.CityListItem>,
    onCityClick: (City) -> Unit,
    isRegionVisible: Boolean,
    errorState: ErrorState?,
    onRefreshClick: () -> Unit,
    onCloseClick: () -> Unit,
    isSnackbarVisible: Boolean,
    snackbarText: Text,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground),
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
        SearchBar(
            isSearchLoadingVisible = isSearchLoadingVisible,
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier.fillMaxWidth()
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            AnimatedContent(
                targetState = errorState,
                transitionSpec = { fadeIn() with fadeOut() },
                label = "error state",
                modifier = Modifier.fillMaxSize()
            ) { error ->
                if (error != null)
                    ModalError(
                        state = error,
                        onButtonClick = onRefreshClick,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.navigationOrIme.asPaddingValues())
                    )
                else
                    LazyColumn(
                        contentPadding = WindowInsets.navigationOrIme.asPaddingValues(),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        cityItems.forEach { item ->
                            when (item) {
                                is CitySelectionViewModel.CityListItem.Header -> stickyHeader(
                                    key = item.key,
                                    contentType = item.contentType,
                                ) {
                                    CityHeader(
                                        text = item.letter,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }

                                is CitySelectionViewModel.CityListItem.Item -> item(
                                    key = item.key,
                                    contentType = item.contentType,
                                ) {
                                    val cityModifier = Modifier
                                        .fillMaxWidth()
                                        .background(UiKitTheme.colors.screenBackground)
                                        .clickable(onClick = { onCityClick(item.city) })
                                        .animateItemPlacement()
                                    if (isRegionVisible)
                                        CityExtendedItem(
                                            city = item.city,
                                            modifier = cityModifier,
                                        )
                                    else
                                        CitySimpleItem(
                                            city = item.city,
                                            modifier = cityModifier,
                                        )
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
    }
}

@Composable
private fun CityHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
    Divider(
        color = UiKitTheme.colors.listDivider,
        thickness = Dp.Hairline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Text(
        text = text,
        style = UiKitTheme.typography.listHeaderItem,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = modifier
            .background(UiKitTheme.colors.screenBackground)
            .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp),
    )
}

@Composable
private fun CitySimpleItem(
    city: City,
    modifier: Modifier = Modifier,
) {
    Divider(
        color = UiKitTheme.colors.listDivider,
        thickness = Dp.Hairline,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
    )
    Text(
        text = city.name,
        style = UiKitTheme.typography.listRegularItem,
        color = UiKitTheme.colors.primaryContentColor,
        textAlign = TextAlign.Start,
        modifier = modifier.padding(16.dp),
    )
}

@Composable
private fun CityExtendedItem(
    city: City,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = city.name,
            style = UiKitTheme.typography.listRegularItem,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = city.region,
            style = UiKitTheme.typography.listRegularItemSubtitle,
            color = UiKitTheme.colors.listItemSubtitle,
            textAlign = TextAlign.Start,
        )
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
            .background(UiKitTheme.colors.screenBackground)
            .padding(horizontal = 16.dp),
    ) {
        AnimatedContent(
            targetState = isSearchLoadingVisible,
            label = "search loader visibility"
        ) { isLoadingVisible ->
            if (isLoadingVisible)
                CircularProgressIndicator(
                    color = UiKitTheme.colors.primaryContentColor,
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
                    style = UiKitTheme.typography.hint,
                    color = UiKitTheme.colors.hint,
                    textAlign = TextAlign.Start,
                    maxLines = 1,
                )
            }
            BasicTextField(
                value = query,
                onValueChange = { onQueryChange(it) },
                textStyle = UiKitTheme.typography.input.copy(color = UiKitTheme.colors.primaryContentColor),
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

@Composable
fun CitySelectionScreen(
    showHome: () -> Unit,
) {
    val viewModel = hiltViewModel<CitySelectionViewModel>()

    val isSearchLoadingVisible by viewModel.isSearchLoadingVisible.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()
    val cityItems by viewModel.cities.collectAsStateWithLifecycle()
    val isRegionVisible by viewModel.isRegionVisible.collectAsStateWithLifecycle()
    val errorState by viewModel.errorState.collectAsStateWithLifecycle()
    val isSnackbarVisible by viewModel.isSnackbarVisible.collectAsStateWithLifecycle()
    val snackbarText by viewModel.snackbarText.collectAsStateWithLifecycle()

    CitySelectionScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showHome = showHome,
    )

    CitySelectionScreenContent(
        isSearchLoadingVisible = isSearchLoadingVisible,
        query = query,
        onQueryChange = viewModel::onQueryChange,
        cityItems = cityItems,
        onCityClick = viewModel::onCityClick,
        isRegionVisible = isRegionVisible,
        errorState = errorState,
        onRefreshClick = viewModel::onRefreshClick,
        onCloseClick = viewModel::onCloseClick,
        isSnackbarVisible = isSnackbarVisible,
        snackbarText = snackbarText,
    )
}

@Composable
fun CitySelectionScreenBehavior(
    sideEffects: Flow<CitySelectionViewModel.SideEffect>,
    showHome: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                CitySelectionViewModel.SideEffect.ShowHome -> showHome()
            }
        }
    }
}

@Preview
@FontScalePreviews
@DensityPreviews
@Composable
fun CitySelectionScreenContentPreview(
    @PreviewParameter(CityListItemProvider::class, limit = 1)
    cityItems: ImmutableList<CitySelectionViewModel.CityListItem>,
) {
    ZarinaTheme {
        CitySelectionScreenContent(
            isSearchLoadingVisible = true,
            query = "",
            onQueryChange = {},
            errorState = null,
            cityItems = cityItems,
            onCityClick = {},
            isRegionVisible = true,
            onRefreshClick = {},
            onCloseClick = {},
            isSnackbarVisible = false,
            snackbarText = Text.Empty,
        )
    }
}

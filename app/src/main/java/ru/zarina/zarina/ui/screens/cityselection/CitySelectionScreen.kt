package ru.zarina.zarina.ui.screens.cityselection

import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.add
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.ui.CityListItemProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CitySelectionScreenContent(
    query: String,
    onQueryChange: (String) -> Unit,
    cityItems: List<CitySelectionViewModel.CityListItem>,
    isRegionVisible: Boolean,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground)
            .statusBarsPadding(),
    ) {
        SearchBar(
            query = query,
            onQueryChange = onQueryChange,
            modifier = Modifier.fillMaxWidth()
        )
        LazyColumn(
            contentPadding = WindowInsets.ime.add(WindowInsets.navigationBars).asPaddingValues(),
            modifier = Modifier.weight(1f),
        ) {
            cityItems.forEach { item ->
                when (item) {
                    is CitySelectionViewModel.CityListItem.Header -> stickyHeader(
                        key = item.key,
                        contentType = item.contentType,
                    ) {
                        Divider(
                            color = UiKitTheme.colors.listDivider,
                            thickness = Dp.Hairline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                        CityHeader(
                            text = item.letter,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }

                    is CitySelectionViewModel.CityListItem.Item -> item(
                        key = item.key,
                        contentType = item.contentType,
                    ) {
                        Divider(
                            color = UiKitTheme.colors.listDivider,
                            thickness = Dp.Hairline,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                        if (isRegionVisible)
                            CityExtendedItem(
                                city = item.city,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        else
                            CitySimpleItem(
                                city = item.city,
                                modifier = Modifier.fillMaxWidth(),
                            )
                    }
                }
            }
        }
    }
}

@Composable
private fun CityHeader(
    text: String,
    modifier: Modifier = Modifier,
) {
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


@Composable
private fun SearchBar(
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
        Icon(
            painter = painterResource(R.drawable.ic_search_24),
            contentDescription = null,
        )
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
    }
}

@Composable
fun CitySelectionScreen() {
    val viewModel = hiltViewModel<CitySelectionViewModel>()

    val query by viewModel.query.collectAsStateWithLifecycle()
    val cityItems by viewModel.cities.collectAsStateWithLifecycle()
    val isRegionVisible by viewModel.isRegionVisible.collectAsStateWithLifecycle()

    CitySelectionScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    CitySelectionScreenContent(
        query = query,
        onQueryChange = viewModel::onQueryChange,
        cityItems = cityItems,
        isRegionVisible = isRegionVisible
    )
}

@Composable
fun CitySelectionScreenBehavior(
    sideEffects: Flow<CitySelectionViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                else -> TODO()
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
    cityItems: List<CitySelectionViewModel.CityListItem>,
) {
    ZarinaTheme {
        CitySelectionScreenContent(
            query = "",
            onQueryChange = {},
            cityItems = cityItems,
            isRegionVisible = true,
        )
    }
}

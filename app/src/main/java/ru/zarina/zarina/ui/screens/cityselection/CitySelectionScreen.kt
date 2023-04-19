package ru.zarina.zarina.ui.screens.cityselection

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.ui.common.tooling.preview.DensityPreviews
import ru.zarina.zarina.ui.common.tooling.preview.FontScalePreviews
import ru.zarina.zarina.ui.common.tooling.preview.providers.ui.CityListItemProvider
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CitySelectionScreenContent(
    cityItems: List<CitySelectionViewModel.CityListItem>,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.screenBackground)
            .systemBarsPadding(),
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
                    CityItem(
                        city = item.city,
                        modifier = Modifier.fillMaxWidth(),
                    )
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
private fun CityItem(
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
fun CitySelectionScreen() {
    val viewModel = hiltViewModel<CitySelectionViewModel>()

    val cityItems by viewModel.cities.collectAsStateWithLifecycle()

    CitySelectionScreenBehavior(
        sideEffects = viewModel.sideEffects
    )

    CitySelectionScreenContent(
        cityItems = cityItems
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
            cityItems = cityItems,
        )
    }
}

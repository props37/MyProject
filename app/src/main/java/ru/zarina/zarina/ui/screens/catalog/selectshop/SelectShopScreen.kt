package ru.zarina.zarina.ui.screens.catalog.selectshop

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Shop
import ru.zarina.zarina.ui.common.components.CityPicker
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.common.components.toolbar.BackButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.theme.UiKitTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun SelectShopScreenContent(
    city: City?,
    onCityClick: () -> Unit,
    shops: ImmutableList<Shop>,
    selectedShop: Shop?,
    onShopClick: (Shop) -> Unit,
    isLoaderVisible: Boolean,
    isApplyButtonVisible: Boolean,
    onApplyClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(id = R.string.selection_of_store),
                startIcon = {
                    BackButton(onClick = onBackClick)
                },
                isElevated = listState.canScrollBackward
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding(),
        ) {
            CityPicker(
                city = city,
                onClick = onCityClick,
                modifier = Modifier.fillMaxWidth(),
            )
            Box(
                modifier = Modifier.weight(1f)
            ) {
                AnimatedContent(
                    targetState = isLoaderVisible,
                    label = "is loader visible",
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (it) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            CircularProgressIndicator(
                                color = UiKitTheme.colors.primaryContentColor,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            state = listState,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            items(
                                items = shops,
                                key = { shop -> shop.id },
                            ) { shop ->
                                ShopItem(
                                    shop = shop,
                                    isSelected = shop == selectedShop,
                                    onClick = { onShopClick(shop) },
                                )
                            }
                        }
                    }
                }
            }
            AnimatedContent(
                targetState = isApplyButtonVisible,
                label = "is apply button visible",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                if (it)
                    ZarinaTextButton(
                        text = stringResource(id = R.string.apply),
                        onClick = onApplyClick,
                        modifier = Modifier.fillMaxWidth(),
                    )
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun ShopItem(
    shop: Shop,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .clickable(onClick = onClick)
            .height(IntrinsicSize.Min)
            .padding(16.dp),
    ) {
        Text(
            text = shop.name,
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            modifier = Modifier.padding(end = 8.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimatedContent(
            targetState = isSelected,
            label = "${shop.id} is selected",
        ) {
            if (it)
                Icon(
                    painter = painterResource(id = R.drawable.ic_checkmark_24),
                    contentDescription = stringResource(id = R.string.selected),
                    tint = UiKitTheme.colors.primaryContentColor,
                )
        }
    }
}

@Composable
fun SelectShopScreen(
    savedStateHandle: SavedStateHandle,
    filtersSavedStateHandle: SavedStateHandle,
    showSelectCity: () -> Unit,
    goBack: () -> Unit
) {
    val viewModel = koinViewModel<SelectShopViewModel> {
        parametersOf(
            savedStateHandle,
            filtersSavedStateHandle
        )
    }

    val city by viewModel.city.collectAsStateWithLifecycle()
    val shops by viewModel.shops.collectAsStateWithLifecycle()
    val selectedShop by viewModel.selectedShop.collectAsStateWithLifecycle()
    val isLoaderVisible by viewModel.isLoaderVisible.collectAsStateWithLifecycle()
    val isApplyButtonVisible by viewModel.isApplyButtonVisible.collectAsStateWithLifecycle()

    SelectShopScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showSelectCity = showSelectCity,
        goBack = goBack,
    )

    SelectShopScreenContent(
        city = city,
        onCityClick = viewModel::onCityClick,
        shops = shops,
        selectedShop = selectedShop,
        onShopClick = viewModel::onShopClick,
        isLoaderVisible = isLoaderVisible,
        onBackClick = viewModel::onBackClick,
        isApplyButtonVisible = isApplyButtonVisible,
        onApplyClick = viewModel::onApplyClick,
    )
}

@Composable
fun SelectShopScreenBehavior(
    sideEffects: Flow<SelectShopViewModel.SideEffect>,
    showSelectCity: () -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                SelectShopViewModel.SideEffect.ShowSelectCity -> showSelectCity()
                SelectShopViewModel.SideEffect.GoBack -> goBack()
            }
        }
    }
}

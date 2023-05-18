package ru.zarina.zarina.ui.screens.pickup.root

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Stock
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.components.DropdownBar
import ru.zarina.zarina.ui.common.components.HorizontalProductCard
import ru.zarina.zarina.ui.common.components.Tabs
import ru.zarina.zarina.ui.common.components.ZarinaScaffold
import ru.zarina.zarina.ui.common.components.toolbar.CloseButton
import ru.zarina.zarina.ui.common.components.toolbar.ScreenToolbar
import ru.zarina.zarina.ui.common.tooling.preview.providers.domain.ProductProvider
import ru.zarina.zarina.ui.screens.pickup.PickupViewModel
import ru.zarina.zarina.ui.screens.pickup.root.components.ShopList
import ru.zarina.zarina.ui.screens.pickup.root.components.ShopMap
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.ui.theme.ZarinaTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun PickupRootScreenContent(
    city: City?,
    product: Product?,
    selectedOffer: Offer?,
    stocks: ImmutableList<Stock>?,
    isLoaderVisible: Boolean,
    onBackClick: () -> Unit,
    onSelectCityClick: () -> Unit,
    onSelectSizeClick: () -> Unit,
    onStockPickupClick: (Stock) -> Unit,
    errorType: PickupViewModel.ErrorType?,
    onRefreshClick: () -> Unit,
) {
    val errorState = when (errorType) {
        PickupViewModel.ErrorType.NETWORK -> ErrorState.NETWORK
        PickupViewModel.ErrorType.GENERIC -> ErrorState.GENERIC
        null -> null
    }
    ZarinaScaffold(
        toolbar = {
            ScreenToolbar(
                title = stringResource(R.string.find_and_pickup),
                endIcon = {
                    CloseButton(onClick = onBackClick)
                }
            )
        },
        isModalLoaderVisible = isLoaderVisible,
        errorState = errorState,
        onErrorButtonClick = onRefreshClick,
    ) {
        if (product != null)
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CityPicker(
                    city = city,
                    onClick = onSelectCityClick,
                )
                HorizontalProductCard(
                    product = product,
                    selectedSize = selectedOffer?.size,
                    onSelectSizeClick = onSelectSizeClick,
                    modifier = Modifier.fillMaxWidth()
                )

                AnimatedContent(
                    targetState = stocks?.isEmpty() == true,
                    label = "stocks animated content",
                    transitionSpec = { fadeIn() with fadeOut() }
                ) { isNoResults ->
                    if (isNoResults) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(UiKitTheme.colors.screenBackground)
                        ) {
                            Text(
                                text = stringResource(R.string.product_not_available_in_city),
                                style = UiKitTheme.typography.circle1518,
                                color = UiKitTheme.colors.primaryContentColor,
                                textAlign = TextAlign.Start,
                                modifier = Modifier.padding(16.dp),
                            )
                        }
                    } else {
                        ShopListPager(
                            stocks = stocks ?: persistentListOf(),
                            onStockPickupClick = onStockPickupClick,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(UiKitTheme.colors.screenBackground)
                        )
                    }
                }
            }
    }
}

@Composable
private fun CityPicker(
    city: City?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    DropdownBar(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = city?.name.orEmpty(),
            style = UiKitTheme.typography.circle1718,
            color = UiKitTheme.colors.primaryContentColor,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ShopListPager(
    stocks: ImmutableList<Stock>,
    onStockPickupClick: (Stock) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
    ) {
        val coroutineScope = rememberCoroutineScope()
        val tabs = persistentListOf(*ShopListTab.values())
        val pagerState = rememberPagerState()
        Tabs(
            options = tabs,
            selectedOption = tabs[pagerState.currentPage],
            textResolver = {
                val resource = when (it) {
                    ShopListTab.LIST -> R.string.list
                    ShopListTab.MAP -> R.string.map
                }
                stringResource(resource)
            },
            onOptionClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(tabs.indexOf(it))
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        HorizontalPager(
            pageCount = tabs.size,
            state = pagerState,
            beyondBoundsPageCount = 1,
            verticalAlignment = Alignment.Top,
            userScrollEnabled = false,
        ) {
            when (it) {
                ShopListTab.LIST.ordinal -> ShopList(
                    stocks = stocks,
                    onStockClick = onStockPickupClick,
                    modifier = Modifier.fillMaxWidth()
                )

                ShopListTab.MAP.ordinal -> {
                    ShopMap(
                        stocks = stocks,
                        isVisibleForUser = pagerState.currentPage == ShopListTab.MAP.ordinal,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

private enum class ShopListTab { LIST, MAP }

@Composable
fun PickupRootScreen(
    parentEntry: NavBackStackEntry,
    showSelectSize: () -> Unit,
    showSelectCity: () -> Unit,
    showDetails: () -> Unit,
    goBack: () -> Unit,
) {
    val viewModel: PickupRootViewModel = hiltViewModel()
    val parentViewModel = hiltViewModel<PickupViewModel>(parentEntry)

    val city by parentViewModel.city.collectAsStateWithLifecycle()
    val product by parentViewModel.product.collectAsStateWithLifecycle()
    val selectedOffer by parentViewModel.selectedOffer.collectAsStateWithLifecycle()
    val stocks by parentViewModel.stocks.collectAsStateWithLifecycle()
    val errorType by parentViewModel.errorType.collectAsStateWithLifecycle()
    val isLoaderVisible by parentViewModel.isStocksLoaderVisible.collectAsStateWithLifecycle()

    PickupRootScreenBehavior(
        sideEffects = viewModel.sideEffects,
        showSelectSize = showSelectSize,
        showSelectCity = showSelectCity,
        showDetails = showDetails,
        goBack = goBack,
    )

    PickupRootScreenContent(
        city = city,
        product = product,
        selectedOffer = selectedOffer,
        stocks = stocks,
        isLoaderVisible = isLoaderVisible,
        onBackClick = viewModel::onBackClick,
        onSelectCityClick = viewModel::onSelectCityClick,
        onSelectSizeClick = viewModel::onSelectSizeClick,
        onStockPickupClick = {
            parentViewModel.onStockPickupClick(it)
            viewModel.onStockPickupClick()
        },
        onRefreshClick = parentViewModel::onRefreshClick,
        errorType = errorType,
    )
}

@Composable
fun PickupRootScreenBehavior(
    sideEffects: Flow<PickupRootViewModel.SideEffect>,
    showSelectSize: () -> Unit,
    showSelectCity: () -> Unit,
    showDetails: () -> Unit,
    goBack: () -> Unit,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { effect ->
            when (effect) {
                PickupRootViewModel.SideEffect.GoBack -> goBack()
                PickupRootViewModel.SideEffect.ShowSelectSize -> showSelectSize()
                PickupRootViewModel.SideEffect.ShowSelectCity -> showSelectCity()
                PickupRootViewModel.SideEffect.ShowDetails -> showDetails()
            }
        }
    }
}

@Preview("multiple sizes")
@Composable
fun PickupRootScreenContentPreview(
    @PreviewParameter(ProductProvider::class, limit = 1)
    product: Product,
) {
    ZarinaTheme {
        PickupRootScreenContent(
            city = City.DEFAULT,
            product = product,
            selectedOffer = product.offers.first(),
            stocks = persistentListOf(),
            isLoaderVisible = false,
            onBackClick = {},
            onSelectCityClick = {},
            onSelectSizeClick = {},
            onStockPickupClick = {},
            onRefreshClick = {},
            errorType = null,
        )
    }
}

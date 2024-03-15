package ru.zarina.zarina.ui.screen.cart

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.bottomnavbar.bottomNavBarPadding
import ru.zarina.zarina.ui.common.base.rememberErrorState
import ru.zarina.zarina.ui.common.component.ProductOrderCard
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.tooling.preview.ZarinaPreview
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.City
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.DeliveryTypePicker
import ru.zarina.zarina.ui.screen.cart.CartScreenComponents.TopBar
import ru.zarina.zarina.ui.screen.cart.CartViewModel.CartState
import ru.zarina.zarina.ui.screen.cart.CartViewModel.SideEffect
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.Crossfade
import ru.zarina.zarina.util.compose.pager.PagerTabRowIntegration

// TODO: [High] Add pull refresh

@Composable
fun CartScreen(
    navigate: (CartScreenAction) -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
) {
    val isCartEmpty by viewModel.isCartEmpty.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val isClearCartButtonVisible by viewModel.isClearCartButtonVisible.collectAsStateWithLifecycle()
    val deliveryTypes by viewModel.deliveryTypes.collectAsStateWithLifecycle()
    val currentDeliveryType by viewModel.currentDeliveryType.collectAsStateWithLifecycle()
    val deliveryTypeToCartState by viewModel.deliveryTypeToCartState.collectAsStateWithLifecycle()

    ScreenContent(
        isCartEmpty = isCartEmpty,
        city = city,
        onCityClicked = viewModel::onCityClicked,
        onGoToCatalogClicked = viewModel::onGoToCatalogClicked,
        isClearCartButtonVisible = isClearCartButtonVisible,
        onClearCartClicked = viewModel::onClearCartClicked,
        deliveryTypes = deliveryTypes,
        currentDeliveryType = currentDeliveryType,
        onDeliveryTypeChanged = viewModel::onDeliveryTypeChanged,
        deliveryTypeToCartState = deliveryTypeToCartState,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ScreenContent(
    isCartEmpty: Boolean,
    city: City?,
    onCityClicked: () -> Unit,
    onGoToCatalogClicked: () -> Unit,
    isClearCartButtonVisible: Boolean,
    onClearCartClicked: () -> Unit,
    deliveryTypes: ImmutableList<DeliveryType>,
    currentDeliveryType: DeliveryType,
    onDeliveryTypeChanged: (DeliveryType) -> Unit,
    deliveryTypeToCartState: ImmutableMap<DeliveryType, StateFlow<CartState>>,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CartScreenAction) -> Unit,
) {
    CartScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .imePadding()
            .bottomNavBarPadding(WindowInsets.ime),
    ) {
        TopBar(
            isClearButtonVisible = isClearCartButtonVisible,
            onClearClicked = onClearCartClicked,
            modifier = Modifier.fillMaxWidth(),
        )

        Crossfade(
            targetState = isCartEmpty,
            modifier = Modifier.fillMaxSize(),
        ) { isCartEmpty ->
            if (!isCartEmpty) {
                // TODO: [High] Extract
                Column {
                    City(
                        city = city,
                        onClick = onCityClicked,
                        modifier = Modifier.fillMaxWidth(),
                    )

                    val pagerState = rememberPagerState { deliveryTypes.size }
                    PagerTabRowIntegration(
                        pagerState = pagerState,
                        tabs = deliveryTypes,
                        currentTab = currentDeliveryType,
                        onCurrentTabChanged = onDeliveryTypeChanged,
                    )

                    DeliveryTypePicker(
                        types = deliveryTypes,
                        currentType = currentDeliveryType,
                        onTypeChanged = onDeliveryTypeChanged,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    // TODO: [High] Extract
                    HorizontalPager(
                        state = pagerState,
                        userScrollEnabled = false,
                        modifier = Modifier.fillMaxSize(),
                    ) { page ->
                        val deliveryType = deliveryTypes[page]
                        val cartState = deliveryTypeToCartState[deliveryType]
                            ?.collectAsStateWithLifecycle()?.value
                            ?: CartState.InitialLoading

                        Crossfade(
                            targetState = cartState,
                            contentKey = {
                                // TODO: [High] Extract
                                when (it) {
                                    is CartState.Cart -> "Cart"
                                    CartState.EmptyCart -> it
                                    is CartState.Error -> it
                                    CartState.InitialLoading -> it
                                }
                            },
                            modifier = Modifier.fillMaxSize(),
                        ) { state ->
                            when (state) {
                                is CartState.Cart -> {
                                    // TODO: [High] Extract
                                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                                        itemsIndexed(
                                            items = state.cart.products,
                                            key = { _, product -> product.id.value },
                                            contentType = { _, product -> null }, // TODO: [High] Implement
                                        ) { index, product ->
                                            ProductOrderCard(
                                                name = product.name,
                                                imageUrl = product.imageUrl,
                                                size = product.size,
                                                sizeRu = null,
                                                height = product.height,
                                                color = product.color,
                                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                            )

                                            if (index < state.cart.products.lastIndex) {
                                                Divider(
                                                    color = UiKitTheme.colors.border.general.default,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .padding(horizontal = 16.dp),
                                                )
                                            }
                                        }
                                    }
                                }

                                CartState.InitialLoading -> Unit // TODO: [High] Implement

                                CartState.EmptyCart -> {
                                    val iconResId: Int
                                    val titleResId: Int
                                    val bodyResId: Int
                                    when (deliveryType) {
                                        DeliveryType.DELIVERY -> {
                                            iconResId = R.drawable.ic_delivery_24
                                            titleResId = R.string.cart_screen_empty_delivery_cart_placeholder_title
                                            bodyResId = R.string.cart_screen_empty_delivery_cart_placeholder_body
                                        }

                                        DeliveryType.PICK_UP_FROM_SHOP -> {
                                            iconResId = R.drawable.ic_shop_24
                                            titleResId = R.string.cart_screen_empty_pick_up_from_shop_cart_placeholder_title
                                            bodyResId = R.string.cart_screen_empty_pick_up_from_shop_cart_placeholder_body
                                        }
                                    }
                                    val errorState = rememberErrorState(
                                        iconResId = iconResId,
                                        title = stringResource(titleResId),
                                        body = stringResource(bodyResId),
                                        buttonText = stringResource(R.string.go_to_catalog),
                                    )
                                    ZarinaErrorScreen(
                                        state = errorState,
                                        onRefreshClicked = onGoToCatalogClicked,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .verticalScroll(rememberScrollState())
                                            .padding(16.dp),
                                    )
                                }

                                is CartState.Error -> Unit // TODO: [High] Implement
                            }
                        }
                    }
                }
            } else {
                val errorState = rememberErrorState(
                    iconResId = R.drawable.ic_cart_outline_64,
                    title = stringResource(R.string.cart_screen_empty_cart_placeholder_title),
                    body = stringResource(R.string.cart_screen_empty_cart_placeholder_description),
                    buttonText = stringResource(R.string.go_to_catalog),
                )
                ZarinaErrorScreen(
                    state = errorState,
                    onRefreshClicked = onGoToCatalogClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                )
            }
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}

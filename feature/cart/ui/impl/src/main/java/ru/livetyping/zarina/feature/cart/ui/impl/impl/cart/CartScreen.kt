package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.domain.model.cart.CartSize
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uicomponent.ZarinaClubModalBottomSheet
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorButtonState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.overlay.ZarinaRefreshingOverlay
import ru.livetyping.zarina.core.uikit.pullrefresh.ZarinaPullRefreshIndicator
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.component.CartScreenComponents.CartContent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.component.CartScreenComponents.TopBar
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.ProductCardActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector.ProductCountSelectorEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector.ProductCountSelectorModalBottomSheet
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.productcountselector.ProductCountSelectorState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun CartScreen(
    navActions: CartNavActions,
    viewModel: CartViewModel,
) {
    val cartProductCount by viewModel.cartProductCount.collectAsStateWithLifecycle()
    val cartSize by viewModel.cartSize.collectAsStateWithLifecycle()
    val city by viewModel.city.collectAsStateWithLifecycle()
    val isClearCartButtonVisible by viewModel.isClearCartButtonVisible.collectAsStateWithLifecycle()
    val cartTypes by viewModel.cartTypes.collectAsStateWithLifecycle()
    val currentCartType by viewModel.currentCartType.collectAsStateWithLifecycle()
    val deliveryCartState = viewModel.deliveryCartState.collectAsStateWithLifecycle()
    val pickupCartState = viewModel.pickupCartState.collectAsStateWithLifecycle()
    val productCardActions = remember(viewModel) {
        ProductCardActions(
            onProductClicked = viewModel::onProductClicked,
            onCountClicked = viewModel::onProductCountClicked,
            onAddToFavoritesClicked = viewModel::onAddProductToWishlistClicked,
            onDeleteFromCartClicked = viewModel::onDeleteProductFromCartClicked,
        )
    }
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val isPullRefreshing by viewModel.isPullRefreshing.collectAsStateWithLifecycle()
    val productCountSelectorState by viewModel.productCountSelectorState.collectAsStateWithLifecycle()

    ScreenContent(
        cartProductCount = cartProductCount,
        cartSize = cartSize,
        city = city,
        onCityClicked = viewModel::onCityClicked,
        onGoToCatalogClicked = viewModel::onGoToCatalogClicked,
        isClearCartButtonVisible = isClearCartButtonVisible,
        onClearCartClicked = viewModel::onClearCartClicked,
        cartTypes = cartTypes,
        currentCartType = currentCartType,
        onCartTypeChanged = viewModel::onCartTypeChanged,
        deliveryCartState = deliveryCartState,
        pickupCartState = pickupCartState,
        onCartErrorRefreshClicked = viewModel::onCartErrorRefreshClicked,
        productCardActions = productCardActions,
        isRefreshing = isRefreshing,
        onPullRefreshTriggered = viewModel::onPullRefreshTriggered,
        isPullRefreshing = isPullRefreshing,
        onIsBonusRedemptionAppliedChanged = viewModel::onIsBonusRedemptionAppliedChanged,
        onBonusCountToRedeemChanged = viewModel::onBonusCountToRedeemChanged,
        onIsMyCardAppliedChanged = viewModel::onIsMyCardAppliedChanged,
        onApplyPromoCodeClicked = viewModel::onApplyPromoCodeClicked,
        onWithdrawPromoCodeClicked = viewModel::onWithdrawPromoCodeClicked,
        onPromoCodeImeDoneClicked = viewModel::onPromoCodeImeDoneClicked,
        onCheckoutClicked = viewModel::onCheckoutClicked,
        onScreenCreated = viewModel::onScreenCreated,
        onBackClicked = viewModel::onBackClicked,
        productCountSelectorState = productCountSelectorState,
        onProductCountSelectorEvent = viewModel::onProductCountSelectorEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun ScreenContent(
    cartProductCount: Int,
    cartSize: CartSize,
    city: City?,
    onCityClicked: () -> Unit,
    onGoToCatalogClicked: () -> Unit,
    isClearCartButtonVisible: Boolean,
    onClearCartClicked: () -> Unit,
    cartTypes: ImmutableList<CartType>,
    currentCartType: CartType,
    onCartTypeChanged: (CartType) -> Unit,
    deliveryCartState: State<CartState>,
    pickupCartState: State<CartState>,
    onCartErrorRefreshClicked: () -> Unit,
    productCardActions: ProductCardActions,
    isRefreshing: Boolean,
    onPullRefreshTriggered: () -> Unit,
    isPullRefreshing: Boolean,
    onIsBonusRedemptionAppliedChanged: (Boolean) -> Unit,
    onBonusCountToRedeemChanged: (Int?) -> Unit,
    onIsMyCardAppliedChanged: (Boolean) -> Unit,
    onApplyPromoCodeClicked: () -> Unit,
    onWithdrawPromoCodeClicked: () -> Unit,
    onPromoCodeImeDoneClicked: () -> Unit,
    onCheckoutClicked: () -> Unit,
    onScreenCreated: () -> Unit,
    onBackClicked: () -> Unit,
    productCountSelectorState: ProductCountSelectorState,
    onProductCountSelectorEvent: (ProductCountSelectorEvent) -> Unit,
    sideEffects: Flow<CartSideEffect>,
    navActions: CartNavActions,
) {
    CartScreenBehavior(
        onBackClicked = onBackClicked,
        onScreenCreated = onScreenCreated,
        sideEffects = sideEffects,
        navActions = navActions,
    )

    var isZarinaClubBottomSheetVisible by remember { mutableStateOf(false) }
    if (isZarinaClubBottomSheetVisible) {
        ZarinaClubModalBottomSheet(
            onDismissRequest = { isZarinaClubBottomSheetVisible = false },
        )
    }

    ProductCountSelectorModalBottomSheet(
        state = productCountSelectorState,
        onEvent = onProductCountSelectorEvent,
    )

    Box {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(UiKitTheme.colors.background.general.regular.default)
                .windowInsetsPadding(
                    WindowInsets.statusBars
                        .union(WindowInsets.displayCutout)
                        .union(WindowInsets.ime),
                )
                .bottomNavBarPadding(WindowInsets.ime),
        ) {
            TopBar(
                isClearButtonVisible = isClearCartButtonVisible,
                onClearClicked = onClearCartClicked,
            )

            Box {
                val pullRefreshState = rememberPullRefreshState(
                    refreshing = isPullRefreshing,
                    onRefresh = onPullRefreshTriggered,
                )
                ZarinaPullRefreshIndicator(
                    isRefreshing = isPullRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .zIndex(1f),
                )

                Crossfade(
                    targetState = cartProductCount == 0,
                    modifier = Modifier
                        .fillMaxSize()
                        .pullRefresh(pullRefreshState),
                ) { isCartEmpty ->
                    if (!isCartEmpty) {
                        CartContent(
                            city = city,
                            onCityClicked = onCityClicked,
                            cartSize = cartSize,
                            cartTypes = cartTypes,
                            currentCartType = currentCartType,
                            onCartTypeChanged = onCartTypeChanged,
                            deliveryCartState = deliveryCartState,
                            pickupCartState = pickupCartState,
                            onCartErrorRefreshClicked = onCartErrorRefreshClicked,
                            onGoToCatalogClicked = onGoToCatalogClicked,
                            productCardActions = productCardActions,
                            onBonusAccrualClicked = { isZarinaClubBottomSheetVisible = true },
                            onIsBonusWriteOffAppliedChanged = onIsBonusRedemptionAppliedChanged,
                            onBonusCountToWriteOffChanged = onBonusCountToRedeemChanged,
                            onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                            onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                            onRemovePromoCodeClicked = onWithdrawPromoCodeClicked,
                            onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                            onCheckoutClicked = onCheckoutClicked,
                        )
                    } else {
                        val errorState = rememberZarinaErrorScreenState(
                            iconResId = RCommon.drawable.ic_shopper_outline_64,
                            title = stringResource(R.string.cart_empty_cart_placeholder_title),
                            body = stringResource(R.string.cart_empty_cart_placeholder_description),
                            buttonState = rememberZarinaErrorButtonState(
                                buttonText = stringResource(R.string.cart_to_catalog),
                            ),
                        )

                        ZarinaErrorScreen(
                            state = errorState,
                            onButtonClicked = onGoToCatalogClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                        )
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isRefreshing,
            enter = fadeIn(),
            exit = fadeOut(),
            modifier = Modifier.matchParentSize(),
        ) {
            ZarinaRefreshingOverlay(modifier = Modifier.fillMaxSize())
        }
    }
}

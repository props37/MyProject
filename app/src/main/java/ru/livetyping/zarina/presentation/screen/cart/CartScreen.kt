package ru.livetyping.zarina.presentation.screen.cart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.cart.CartSize
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.common.Url
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaClubBottomSheetContent
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.presentation.common.component.overlay.ZarinaRefreshingOverlay
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.error.rememberErrorState
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.cart.CartScreenComponents.CartContent
import ru.livetyping.zarina.presentation.screen.cart.CartScreenComponents.ProductCardActions
import ru.livetyping.zarina.presentation.screen.cart.CartScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel.CartState
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

// TODO: [Medium] Add pull refresh

@Composable
fun CartScreen(
    navigate: (CartScreenAction) -> Unit,
    viewModel: CartViewModel = hiltViewModel(),
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
            onCountClicked = viewModel::onProductCountClicked,
            onAddToFavoritesClicked = viewModel::onAddProductToFavoritesClicked,
            onDeleteFromCartClicked = viewModel::onDeleteProductFromCartClicked,
        )
    }
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()

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
        onIsBonusWriteOffAppliedChanged = viewModel::onIsBonusWriteOffAppliedChanged,
        onBonusCountToWriteOffChanged = viewModel::onBonusCountToWriteOffChanged,
        onIsMyCardAppliedChanged = viewModel::onIsMyCardAppliedChanged,
        onApplyPromoCodeClicked = viewModel::onApplyPromoCodeClicked,
        onRemovePromoCodeClicked = viewModel::onRemovePromoCodeClicked,
        onPromoCodeImeDoneClicked = viewModel::onPromoCodeImeDoneClicked,
        onUrlClicked = viewModel::onUrlClicked,
        onCheckoutClicked = viewModel::onCheckoutClicked,
        onScreenOpened = viewModel::onScreenOpened,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ScreenContent(
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
    onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
    onBonusCountToWriteOffChanged: (Int) -> Unit,
    onIsMyCardAppliedChanged: (Boolean) -> Unit,
    onApplyPromoCodeClicked: () -> Unit,
    onRemovePromoCodeClicked: () -> Unit,
    onPromoCodeImeDoneClicked: () -> Unit,
    onUrlClicked: (Url) -> Unit,
    onCheckoutClicked: () -> Unit,
    onScreenOpened: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CartScreenAction) -> Unit,
) {
    val coroutineScope = rememberCoroutineScope()

    CartScreenBehavior(
        onScreenOpened = onScreenOpened,
        sideEffects = sideEffects,
        navigate = navigate,
    )

    var isZarinaClubBottomSheetVisible by remember { mutableStateOf(false) }
    val zarinaClubBottomSheetState = rememberModalBottomSheetState()
    if (isZarinaClubBottomSheetVisible) {
        ZarinaModalBottomSheet(
            onDismissRequest = { isZarinaClubBottomSheetVisible = false },
            sheetState = zarinaClubBottomSheetState,
            windowInsets = { WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom) },
        ) {
            ZarinaClubBottomSheetContent(
                onCloseClicked = {
                    coroutineScope
                        .launch { zarinaClubBottomSheetState.hide() }
                        .invokeOnCompletion { isZarinaClubBottomSheetVisible = false }
                },
                onLearnMoreClicked = onUrlClicked,
            )
        }
    }

    Box {
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
                targetState = cartProductCount == 0,
                modifier = Modifier.fillMaxSize(),
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
                        onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                        onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                        onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                        onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                        onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                        onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                        onCheckoutClicked = onCheckoutClicked,
                    )
                } else {
                    val errorState = rememberErrorState(
                        iconResId = R.drawable.ic_shopper_outline_64,
                        title = stringResource(R.string.cart_empty_cart_placeholder_title),
                        body = stringResource(R.string.cart_empty_cart_placeholder_description),
                        buttonText = stringResource(R.string.go_to_catalog),
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

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}

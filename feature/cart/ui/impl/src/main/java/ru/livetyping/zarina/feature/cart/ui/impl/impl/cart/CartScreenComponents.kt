package ru.livetyping.zarina.feature.cart.ui.impl.impl.cart

import android.os.Parcelable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.byValue
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.cart.CartSize
import ru.livetyping.zarina.core.domain.model.cart.CartType
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.coercedOffset
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.core.uicompose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.core.uicompose.none
import ru.livetyping.zarina.core.uicompose.price.rememberFormattedPrice
import ru.livetyping.zarina.core.uicompose.rememberAnchoredDraggableState
import ru.livetyping.zarina.core.uicompose.textString
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.button.ZarinaIconButton
import ru.livetyping.zarina.core.uikit.cart.CartPrice
import ru.livetyping.zarina.core.uikit.counter.ZarinaCounter
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorButtonState
import ru.livetyping.zarina.core.uikit.error.rememberZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.product.ProductOrderCard
import ru.livetyping.zarina.core.uikit.product.ProductOrderCardCountStyle
import ru.livetyping.zarina.core.uikit.product.ProductOrderCardSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.switchh.ZarinaSwitch
import ru.livetyping.zarina.core.uikit.tab.ZarinaTab
import ru.livetyping.zarina.core.uikit.tab.ZarinaTabRow
import ru.livetyping.zarina.core.uikit.text.ZarinaPromoCodeTextField
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartBonusAccountState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartMyCardState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartProductItem
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.ProductCardActions
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.PagerTabRowIntegration
import kotlin.math.roundToInt
import ru.livetyping.zarina.core.resource.R as RCommon

@Suppress("ConstPropertyName")
internal object CartScreenComponents {

    @Composable
    fun TopBar(
        isClearButtonVisible: Boolean,
        onClearClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(RCommon.string.res_cart),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isClearButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = onClearClicked,
                        size = ZarinaButtonSize.Small,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        textStyle = UiKitTheme.typography.caption1.regular,
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(text = stringResource(RCommon.string.res_clear).uppercase())
                    }
                }
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    fun CartContent(
        city: City?,
        onCityClicked: () -> Unit,
        cartSize: CartSize,
        cartTypes: ImmutableList<CartType>,
        currentCartType: CartType,
        onCartTypeChanged: (CartType) -> Unit,
        deliveryCartState: State<CartState>,
        pickupCartState: State<CartState>,
        onCartErrorRefreshClicked: () -> Unit,
        onGoToCatalogClicked: () -> Unit,
        productCardActions: ProductCardActions,
        onBonusAccrualClicked: () -> Unit,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        onCheckoutClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val cityScrollBehavior = CollapsingTopBarDefaults.rememberExitUntilCollapsedScrollBehavior()
        CollapsingTopBarLayout(
            topBar = {
                City(
                    city = city,
                    onClick = onCityClicked,
                    modifier = Modifier.fillMaxWidth(),
                )
            },
            scrollBehavior = cityScrollBehavior,
            modifier = modifier.clipToBounds(),
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .nestedScroll(cityScrollBehavior.nestedScrollConnection),
            ) {
                val pagerState = rememberPagerState { cartTypes.size }
                PagerTabRowIntegration(
                    pagerState = pagerState,
                    tabs = cartTypes,
                    currentTab = currentCartType,
                    onCurrentTabChanged = onCartTypeChanged,
                )

                CartTypePicker(
                    types = cartTypes,
                    currentType = currentCartType,
                    onTypeChanged = onCartTypeChanged,
                    cartSize = cartSize,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                CartTypeContentPager(
                    pagerState = pagerState,
                    cartTypes = cartTypes,
                    deliveryCartState = deliveryCartState,
                    pickupCartState = pickupCartState,
                    onGoToCatalogClicked = onGoToCatalogClicked,
                    onCartErrorRefreshClicked = onCartErrorRefreshClicked,
                    productCardActions = productCardActions,
                    onBonusAccrualClicked = onBonusAccrualClicked,
                    onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                    onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                    onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                    onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                    onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                    onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                    onCheckoutClicked = onCheckoutClicked,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }

    @Composable
    private fun City(
        city: City?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        Crossfade(
            targetState = city,
            modifier = modifier.clickable(
                enabled = city != null,
                onClick = onClick,
            ),
        ) { city ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                if (city != null) {
                    Text(
                        text = city.name,
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f),
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_small_arrow_up_24),
                        contentDescription = stringResource(RCommon.string.res_change_city),
                        tint = UiKitTheme.colors.icon.regular.default,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(degrees = 90f),
                    )
                } else {
                    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
                    ZarinaTextSkeleton(
                        textStyle = UiKitTheme.typography.secondary.light,
                        shimmer = shimmer,
                        modifier = Modifier.fillMaxWidth(fraction = 0.55f),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ZarinaSkeleton(
                        shimmer = shimmer,
                        modifier = Modifier.size(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun CartTypePicker(
        types: ImmutableList<CartType>,
        currentType: CartType,
        onTypeChanged: (CartType) -> Unit,
        cartSize: CartSize,
        modifier: Modifier = Modifier,
    ) {
        val selectedTabIndex = remember(types, currentType) {
            types.indexOf(currentType)
        }

        ZarinaTabRow(
            selectedTabIndex = selectedTabIndex,
            modifier = modifier,
        ) {
            types.forEach { type ->
                val productCount = when (type) {
                    CartType.DELIVERY -> cartSize.deliveryProductCount
                    CartType.PICKUP -> cartSize.pickupProductCount
                }
                CartTypeTab(
                    type = type,
                    onClick = { onTypeChanged(type) },
                    isSelected = type == currentType,
                    productCount = productCount,
                )
            }
        }
    }

    @Composable
    private fun CartTypeTab(
        type: CartType,
        onClick: () -> Unit,
        isSelected: Boolean,
        productCount: Int,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTab(
            onClick = onClick,
            isSelected = isSelected,
            modifier = modifier,
        ) {
            val textResId = when (type) {
                CartType.DELIVERY -> RCommon.string.res_delivery
                CartType.PICKUP -> RCommon.string.res_from_store
            }

            val style = if (isSelected) {
                UiKitTheme.typography.secondary.regular
            } else {
                UiKitTheme.typography.secondary.light
            }

            Text(
                text = stringResource(textResId),
                style = style,
                color = UiKitTheme.colors.text.general.regular.default,
            )

            AnimatedContent(
                targetState = productCount,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "CartTypeButton product count",
            ) { count ->
                if (count > 0) {
                    ZarinaCounter(
                        value = count.toString(),
                        modifier = Modifier.padding(start = 8.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun CartTypeContentPager(
        pagerState: PagerState,
        cartTypes: ImmutableList<CartType>,
        deliveryCartState: State<CartState>,
        pickupCartState: State<CartState>,
        onGoToCatalogClicked: () -> Unit,
        onCartErrorRefreshClicked: () -> Unit,
        productCardActions: ProductCardActions,
        onBonusAccrualClicked: () -> Unit,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        onCheckoutClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val cartType = cartTypes[page]
            val cartState = when (cartType) {
                CartType.DELIVERY -> deliveryCartState
                CartType.PICKUP -> pickupCartState
            }.value

            Crossfade(
                targetState = cartState,
                contentKey = ::getCartTypePagerContentKey,
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                when (state) {
                    is CartState.Cart -> {
                        Cart(
                            cartState = state,
                            productCardActions = productCardActions,
                            onBonusAccrualClicked = onBonusAccrualClicked,
                            onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                            onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                            onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                            onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                            onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                            onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                            onCheckoutClicked = onCheckoutClicked,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    CartState.Loading -> {
                        CartSkeleton()
                    }

                    CartState.EmptyCart -> {
                        EmptyCartPlaceholder(
                            cartType = cartType,
                            onGoToCatalogClicked = onGoToCatalogClicked,
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(rememberScrollState())
                                .padding(16.dp),
                        )
                    }

                    is CartState.Error -> {
                        ZarinaErrorScreen(
                            state = state.state,
                            onButtonClicked = onCartErrorRefreshClicked,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    private fun Cart(
        cartState: CartState.Cart,
        productCardActions: ProductCardActions,
        onBonusAccrualClicked: () -> Unit,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        onCheckoutClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            val lazyListState = rememberLazyListState()

            CartList(
                cartState = cartState,
                productCardActions = productCardActions,
                lazyListState = lazyListState,
                onBonusAccrualClicked = onBonusAccrualClicked,
                onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                onCheckoutClicked = onCheckoutClicked,
                modifier = Modifier.matchParentSize(),
            )

            val isCheckoutBlockVisible by remember {
                derivedStateOf {
                    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.contentType == CartContentType.CheckoutBlock }
                }
            }

            CartBottomFloatingBlock(
                isVisible = !isCheckoutBlockVisible && !WindowInsets.isImeVisible,
                finalPrice = cartState.price.finalPrice,
                buttonText = stringResource(RCommon.string.res_checkout).uppercase(),
                isButtonEnabled = !cartState.productLimit.isExceeded,
                onButtonClicked = onCheckoutClicked,
                modifier = Modifier.align(Alignment.BottomCenter),
            )
        }
    }

    @Composable
    private fun CartList(
        cartState: CartState.Cart,
        productCardActions: ProductCardActions,
        lazyListState: LazyListState,
        onBonusAccrualClicked: () -> Unit,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        onCheckoutClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        var lastDraggedProductId by remember { mutableStateOf<CartProduct.Id?>(null) }

        LazyColumn(
            state = lazyListState,
            modifier = modifier,
        ) {
            itemsIndexed(
                items = cartState.productItems,
                key = { _, item -> getCartProductItemKey(item) },
                contentType = { _, _ -> CartContentType.Product },
            ) { index, item ->
                SwipeableProductOrderCard(
                    productItem = item,
                    productCardActions = productCardActions,
                    isDividerVisible = index < cartState.productItems.lastIndex,
                    onDragStarted = { lastDraggedProductId = it },
                    lastDraggedProductId = lastDraggedProductId,
                    onResetSwipeState = { lastDraggedProductId = null },
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateZarinaItem(this),
                )
            }

            if (cartState.bonusAccountState.bonusAccount.addForPurchase != 0) {
                item(
                    key = CartKey.BonusAccrual,
                    contentType = CartContentType.BonusAccrual,
                ) {
                    BonusAccrual(
                        bonusCount = cartState.bonusAccountState.bonusAccount.addForPurchase,
                        onClick = onBonusAccrualClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                            .padding(start = 16.dp, end = 8.dp)
                            .animateZarinaItem(this),
                    )
                }
            }

            if (cartState.bonusAccountState.isRedemptionAvailable) {
                item(
                    key = CartKey.BonusWriteOff,
                    contentType = CartContentType.BonusWriteOff,
                ) {
                    BonusRedemption(
                        state = cartState.bonusAccountState,
                        onIsAppliedChanged = onIsBonusWriteOffAppliedChanged,
                        onBonusCountToRedeemChanged = onBonusCountToWriteOffChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .padding(start = 16.dp, end = 8.dp)
                            .animateZarinaItem(this),
                    )
                }
            }

            if (cartState.myCardState != null) {
                item(
                    key = CartKey.MyCard,
                    contentType = CartContentType.MyCard,
                ) {
                    MyCard(
                        state = cartState.myCardState,
                        onIsAppliedChanged = onIsMyCardAppliedChanged,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .padding(start = 16.dp, end = 8.dp)
                            .animateZarinaItem(this),
                    )
                }
            }

            if (cartState.promoCodeState != null) {
                item(
                    key = CartKey.PromoCode,
                    contentType = CartContentType.PromoCode,
                ) {
                    ZarinaPromoCodeTextField(
                        state = cartState.promoCodeState.textFieldState,
                        isApplied = cartState.promoCodeState.isApplied,
                        appliedPromoCode = cartState.promoCodeState.appliedPromoCode,
                        onApplyClicked = onApplyPromoCodeClicked,
                        onRemoveClicked = onRemovePromoCodeClicked,
                        isError = cartState.promoCodeState.isInvalid,
                        description = {
                            PromoCodeDescription(
                                text = cartState.promoCodeState.description?.let { textString(it) },
                            )
                        },
                        onKeyboardAction = { onPromoCodeImeDoneClicked() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .padding(horizontal = 16.dp)
                            .animateZarinaItem(this),
                    )
                }
            }

            item(
                key = CartKey.Price,
                contentType = CartContentType.Price,
            ) {
                CartPrice(
                    cartPrice = cartState.price.cartPrice,
                    discountSize = cartState.price.discountSize,
                    isDeliveryPriceIncluded = false,
                    deliveryPrice = null,
                    giftCertificateWriteOffSize = cartState.price.giftCertificateRedemptionValue,
                    finalPrice = cartState.price.finalPrice,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .animateZarinaItem(this),
                )
            }

            if (cartState.productLimit.isExceeded) {
                item(
                    key = CartKey.ProductLimitExceededError,
                    contentType = CartContentType.ProductLimitExceededError,
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .padding(horizontal = 16.dp)
                            .animateZarinaItem(this),
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(RCommon.drawable.ic_question_mark_shaped_24),
                            contentDescription = stringResource(R.string.cart_product_limit_exceeded_error_content_description),
                            tint = UiKitTheme.colors.icon.regular.error,
                            modifier = Modifier.size(16.dp),
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(
                                id = R.string.cart_product_limit_exceeded_error_text,
                                cartState.productLimit.limit
                            ),
                            style = UiKitTheme.typography.footnote.light,
                            color = UiKitTheme.colors.text.general.accent.red,
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            item(
                key = CartKey.CheckoutBlock,
                contentType = CartContentType.CheckoutBlock,
            ) {
                ZarinaButton(
                    onClick = onCheckoutClicked,
                    isEnabled = !cartState.productLimit.isExceeded,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 20.dp)
                        .animateZarinaItem(this),
                ) {
                    Text(text = stringResource(RCommon.string.res_checkout).uppercase())
                }
            }
        }
    }

    @Composable
    fun CartBottomFloatingBlock(
        isVisible: Boolean,
        finalPrice: Int,
        buttonText: String,
        isButtonEnabled: Boolean,
        onButtonClicked: () -> Unit,
        modifier: Modifier = Modifier,
        windowInsets: WindowInsets = WindowInsets.none,
        isButtonLoading: Boolean = false,
    ) {
        val animationSpec = remember { spring<IntOffset>(stiffness = Spring.StiffnessMedium) }

        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(animationSpec) { it },
            exit = slideOutVertically(animationSpec) { it },
            modifier = modifier,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UiKitTheme.colors.background.general.regular.default)
                    .windowInsetsPadding(windowInsets)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Column(Modifier.weight(1f)) {
                    val textColor = UiKitTheme.colors.text.general.regular.default
                    Text(
                        text = stringResource(RCommon.string.res_total),
                        style = UiKitTheme.typography.tertiary.light,
                        color = textColor,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val formattedPrice = rememberFormattedPrice(finalPrice)
                    Text(
                        text = stringResource(RCommon.string.res_price_in_rubles, formattedPrice),
                        style = UiKitTheme.typography.primary.bold,
                        color = textColor,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                ZarinaButton(
                    onClick = onButtonClicked,
                    isEnabled = isButtonEnabled,
                    isLoading = isButtonLoading,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = buttonText)
                }
            }
        }
    }

    @Composable
    private fun CartSkeleton(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

            repeat(CartProductSkeletonCount) { index ->
                ProductOrderCardSkeleton(shimmer = shimmer)

                if (index < CartProductSkeletonCount - 1) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    @Composable
    private fun EmptyCartPlaceholder(
        cartType: CartType,
        onGoToCatalogClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val iconResId: Int
        val titleResId: Int
        val bodyResId: Int
        when (cartType) {
            CartType.DELIVERY -> {
                iconResId = RCommon.drawable.ic_scooter_64
                titleResId = R.string.cart_empty_delivery_cart_placeholder_title
                bodyResId = R.string.cart_empty_delivery_cart_placeholder_body
            }

            CartType.PICKUP -> {
                iconResId = RCommon.drawable.ic_store_64
                titleResId = R.string.cart_empty_pick_up_from_store_cart_placeholder_title
                bodyResId = R.string.cart_empty_pick_up_from_store_cart_placeholder_body
            }
        }
        val errorState = rememberZarinaErrorScreenState(
            iconResId = iconResId,
            title = stringResource(titleResId),
            body = stringResource(bodyResId),
            buttonState = rememberZarinaErrorButtonState(
                buttonText = stringResource(R.string.cart_to_catalog),
            ),
        )

        ZarinaErrorScreen(
            state = errorState,
            onButtonClicked = onGoToCatalogClicked,
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun SwipeableProductOrderCard(
        productItem: CartProductItem,
        productCardActions: ProductCardActions,
        isDividerVisible: Boolean,
        onDragStarted: (CartProduct.Id) -> Unit,
        lastDraggedProductId: CartProduct.Id?,
        onResetSwipeState: (CartProduct) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val product = productItem.product

        val density = LocalDensity.current
        val anchors = remember(density) {
            with(density) {
                DraggableAnchors {
                    ProductOrderCardSwipeableState.Default at 0f
                    ProductOrderCardSwipeableState.SwipedLeft at
                            (-ProductOrderCardSwipeDistance).toPx()
                }
            }
        }
        val anchoredDraggableState = rememberAnchoredDraggableState(
            initialValue = ProductOrderCardSwipeableState.Default,
            anchors = anchors,
        )

        val anchoredDraggableInteractionSource = remember { MutableInteractionSource() }
        val updatedProductId by rememberUpdatedState(product.id)
        LaunchedEffect(anchoredDraggableInteractionSource) {
            anchoredDraggableInteractionSource.interactions.collect {
                when (it) {
                     is DragInteraction.Start -> onDragStarted(updatedProductId)
                }
            }
        }

        LaunchedEffect(product.id, lastDraggedProductId, anchoredDraggableState) {
            if (product.id != lastDraggedProductId) {
                anchoredDraggableState.animateTo(ProductOrderCardSwipeableState.Default)
            }
        }

        Box(modifier = modifier.height(IntrinsicSize.Min)) {
            Column(
                modifier = Modifier
                    .zIndex(1f)
                    .offset {
                        val xOffset =
                            anchoredDraggableState.coercedOffset.takeIf { !it.isNaN() } ?: 0f
                        IntOffset(x = xOffset.roundToInt(), y = 0)
                    }
                    .anchoredDraggable(
                        state = anchoredDraggableState,
                        orientation = Orientation.Horizontal,
                        interactionSource = anchoredDraggableInteractionSource,
                    ),
            ) {
                val countStyle = remember(productItem, productCardActions.onCountClicked) {
                    ProductOrderCardCountStyle.Selector(
                        isEditable = productItem.availableCount > 1,
                        onClick = { productCardActions.onCountClicked(product) },
                    )
                }

                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl.value,
                    size = product.size,
                    sizeRu = null,
                    height = product.height,
                    color = product.color,
                    count = product.count,
                    countStyle = countStyle,
                    price = product.price,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { productCardActions.onProductClicked(product) },
                )

                if (isDividerVisible) {
                    Box {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(UiKitTheme.colors.background.general.regular.default),
                        )

                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }

            ProductOrderCardSwipeActionButtons(
                product = product,
                onAddToFavoritesClicked = {
                    onResetSwipeState(it)
                    productCardActions.onAddToFavoritesClicked(it)
                },
                onDeleteFromCartClicked = {
                    onResetSwipeState(it)
                    productCardActions.onDeleteFromCartClicked(it)
                },
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .zIndex(zIndex = 0.5f)
                    .width(ProductOrderCardSwipeDistance)
                    .fillMaxHeight(),
            )
        }
    }

    @Composable
    private fun ProductOrderCardSwipeActionButtons(
        product: CartProduct,
        onAddToFavoritesClicked: (CartProduct) -> Unit,
        onDeleteFromCartClicked: (CartProduct) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaButton(
                onClick = { onAddToFavoritesClicked(product) },
                colors = ZarinaButtonDefaults.tertiaryColors(),
                shape = RectangleShape,
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val iconResId: Int
                    val textRedId: Int
                    if (product.isInWishlist) {
                        iconResId = RCommon.drawable.ic_heart_24
                        textRedId = RCommon.string.res_in_wishlist
                    } else {
                        iconResId = RCommon.drawable.ic_heart_outline_24
                        textRedId = RCommon.string.res_to_wishlist
                    }

                    @Suppress("NAME_SHADOWING")
                    Crossfade(
                        targetState = iconResId,
                        label = "Add To Favorites button icon",
                    ) { iconResId ->
                        Icon(
                            imageVector = ImageVector.vectorResource(iconResId),
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))

                    @Suppress("NAME_SHADOWING")
                    Crossfade(
                        targetState = textRedId,
                        label = "Add To Favorites button text",
                    ) { textRedId ->
                        Text(
                            text = stringResource(textRedId).uppercase(),
                            style = UiKitTheme.typography.caption2.regular,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
            }

            ZarinaButton(
                onClick = { onDeleteFromCartClicked(product) },
                shape = RectangleShape,
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_trash_can_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(RCommon.string.res_delete).uppercase(),
                        style = UiKitTheme.typography.caption2.regular,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun BonusAccrual(
        bonusCount: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.clickable(
                interactionSource = null,
                indication = null,
                onClick = onClick,
            ),
        ) {
            val baseText = stringResource(R.string.cart_we_will_award_you_for_purchase)
            val bonusText = pluralStringResource(
                id = RCommon.plurals.res_bonus_count,
                count = bonusCount,
                bonusCount.toString(),
            )
            val baseTextStyle = UiKitTheme.typography.secondary.light
            val bonusTextStyle = UiKitTheme.typography.secondary.regular
            val text = remember(baseText, bonusText, baseTextStyle, bonusTextStyle) {
                buildAnnotatedString {
                    withStyle(baseTextStyle.toSpanStyle()) {
                        append(baseText)
                    }
                    append(" ")
                    withStyle(bonusTextStyle.toSpanStyle()) {
                        append(bonusText)
                    }
                }
            }

            Text(
                text = text,
                style = baseTextStyle,
                modifier = Modifier.weight(1f),
            )

            Spacer(modifier = Modifier.width(8.dp))

            CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                val iconSize = 16.dp
                ZarinaIconButton(
                    onClick = onClick,
                    indication = ripple(bounded = false, radius = iconSize),
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(RCommon.drawable.ic_question_mark_shaped_24),
                        contentDescription = stringResource(RCommon.string.res_for_zarina_club_members),
                        modifier = Modifier.size(iconSize),
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun BonusRedemption(
        state: CartBonusAccountState,
        onIsAppliedChanged: (Boolean) -> Unit,
        onBonusCountToRedeemChanged: (Int?) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.cart_redeem_bonuses),
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val formattedAvailable = rememberFormattedPrice(state.bonusAccount.balance)
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.cart_you_have_bonuses,
                            state.bonusAccount.balance,
                            formattedAvailable
                        ),
                        style = UiKitTheme.typography.footnote.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                ZarinaSwitch(
                    isChecked = state.isRedemptionApplied,
                    onCheckedChanged = onIsAppliedChanged,
                )
            }

            AnimatedContent(
                targetState = state.isRedemptionApplied,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "Bonus write off text field",
                modifier = Modifier.fillMaxWidth(),
            ) { isVisible ->
                if (isVisible) {
                    var isFocused by remember { mutableStateOf(false) }
                    val updatedIsImeVisible by rememberUpdatedState(WindowInsets.isImeVisible)
                    val updatedOnBonusCountToWriteOffChanged by rememberUpdatedState(onBonusCountToRedeemChanged)
                    LaunchedEffect(Unit) {
                        snapshotFlow { updatedIsImeVisible }.collect { isImeVisible ->
                            if (!isImeVisible && isFocused) {
                                val bonusCount =
                                    state.redemptionTextFieldState.text.toString().toIntOrNull()
                                updatedOnBonusCountToWriteOffChanged(bonusCount)
                            }
                        }
                    }

                    ZarinaTextField(
                        state = state.redemptionTextFieldState,
                        description = {
                            val formattedMaxWriteOff = rememberFormattedPrice(state.bonusAccount.redemption.max)
                            Text(
                                text = pluralStringResource(
                                    id = R.plurals.cart_you_can_redeem_bonuses,
                                    state.bonusAccount.redemption.max,
                                    formattedMaxWriteOff,
                                ),
                            )
                        },
                        inputTransformation = InputTransformation.byValue { _, proposed ->
                            proposed
                                .dropWhile { it != '0' }
                                .filter { it.isDigit() }
                        },
                        keyboardOptions = remember {
                            KeyboardOptions(
                                keyboardType = KeyboardType.Decimal,
                                imeAction = ImeAction.Done,
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                            .padding(end = 8.dp)
                            .onFocusChanged { isFocused = it.isFocused },
                    )
                }
            }
        }
    }

    @Composable
    fun MyCard(
        state: CartMyCardState,
        onIsAppliedChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.cart_apply_my_card),
                    style = UiKitTheme.typography.secondary.light,
                    color = UiKitTheme.colors.text.general.regular.default,
                )

                if (state.info != null) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = state.info,
                        style = UiKitTheme.typography.footnote.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            ZarinaSwitch(
                isChecked = state.isApplied,
                onCheckedChanged = onIsAppliedChanged,
            )
        }
    }

    @Composable
    fun PromoCodeDescription(
        text: String?,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        AnimatedContent(
            targetState = text,
            transitionSpec = {
                AnimatedContentDefaultTransitionSpec.using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.Center,
            label = "PromoCode description",
            modifier = modifier,
        ) { text ->
            if (text != null) {
                Text(text = text)
            }
        }
    }

    private fun getCartTypePagerContentKey(cartState: CartState): Any = when (cartState) {
        is CartState.Cart -> CartTypePagerContentKeyCart
        CartState.EmptyCart, is CartState.Error, CartState.Loading -> cartState
    }

    private fun getCartProductItemKey(productItem: CartProductItem): CartKey.Product {
        return CartKey.Product(productItem.product.id.value)
    }

    private const val CartProductSkeletonCount = 6

    private enum class ProductOrderCardSwipeableState { Default, SwipedLeft }

    private const val CartTypePagerContentKeyCart = "CartTypePagerContentKeyCart"

    @Stable
    @Parcelize
    private sealed class CartKey : Parcelable {
        data class Product(val id: String) : CartKey()

        data object BonusAccrual : CartKey()

        data object BonusWriteOff : CartKey()

        data object MyCard : CartKey()

        data object PromoCode : CartKey()

        data object Price : CartKey()

        data object ProductLimitExceededError : CartKey()

        data object CheckoutBlock : CartKey()
    }

    private enum class CartContentType {
        Product,
        BonusAccrual,
        BonusWriteOff,
        MyCard,
        PromoCode,
        Price,
        ProductLimitExceededError,
        CheckoutBlock,
    }

    private val ProductOrderCardSwipeDistance: Dp get() = 120.dp
}
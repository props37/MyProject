package ru.livetyping.zarina.presentation.screen.cart

import android.os.Parcelable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.input.TextFieldState
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.buildAnnotatedString
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
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartSize
import ru.livetyping.zarina.domain.cart.DeliveryType
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.component.CartPrice
import ru.livetyping.zarina.presentation.common.component.ProductOrderCard
import ru.livetyping.zarina.presentation.common.component.ProductOrderCardCountStyle
import ru.livetyping.zarina.presentation.common.component.ProductOrderCardSkeleton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.component.counter.ZarinaCounter
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.switchh.ZarinaSwitch
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPromoCodeTextField
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.error.rememberErrorState
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPrice
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel.CartState
import ru.livetyping.zarina.presentation.screen.cart.CartViewModel.MyCardState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.animation.Crossfade
import ru.livetyping.zarina.util.compose.coercedOffset
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarDefaults
import ru.livetyping.zarina.util.compose.collapsingtopbar.CollapsingTopBarLayout
import ru.livetyping.zarina.util.compose.pager.PagerTabRowIntegration
import ru.livetyping.zarina.util.compose.rememberAnchoredDraggableState
import kotlin.math.roundToInt

@Suppress("ConstPropertyName")
object CartScreenComponents {

    @Composable
    fun TopBar(
        isClearButtonVisible: Boolean,
        onClearClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.cart),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isClearButtonVisible,
                    enter = remember { AnimatedContentDefaultEnterTransition },
                    exit = remember { AnimatedContentDefaultExitTransition },
                ) {
                    ZarinaButton(
                        onClick = onClearClicked,
                        size = ZarinaButtonSize.Small,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        textStyle = UiKitTheme.typography.caption1.regular,
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(text = stringResource(R.string.clear).uppercase())
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
        deliveryTypes: ImmutableList<DeliveryType>,
        currentDeliveryType: DeliveryType,
        onDeliveryTypeChanged: (DeliveryType) -> Unit,
        deliveryCartState: State<CartState>,
        pickUpFromStoreCartState: State<CartState>,
        onCartErrorRefreshClicked: () -> Unit,
        onGoToCatalogClicked: () -> Unit,
        productCardActions: ProductCardActions,
        onBonusAccrualClicked: () -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        promoCodeTextFieldState: TextFieldState,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
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
                    cartSize = cartSize,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                DeliveryTypeContentPager(
                    pagerState = pagerState,
                    deliveryTypes = deliveryTypes,
                    deliveryCartState = deliveryCartState,
                    pickUpFromStoreCartState = pickUpFromStoreCartState,
                    onGoToCatalogClicked = onGoToCatalogClicked,
                    onCartErrorRefreshClicked = onCartErrorRefreshClicked,
                    productCardActions = productCardActions,
                    onBonusAccrualClicked = onBonusAccrualClicked,
                    onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                    promoCodeTextFieldState = promoCodeTextFieldState,
                    onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                    onRemovePromoCodeClicked = onRemovePromoCodeClicked,
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
                        imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                        contentDescription = stringResource(R.string.change_city),
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
    private fun DeliveryTypePicker(
        types: ImmutableList<DeliveryType>,
        currentType: DeliveryType,
        onTypeChanged: (DeliveryType) -> Unit,
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
                    DeliveryType.DELIVERY -> cartSize.deliveryProductCount
                    DeliveryType.PICK_UP_FROM_STORE -> cartSize.pickUpFromStoreProductCount
                }
                DeliveryTypeTab(
                    type = type,
                    onClick = { onTypeChanged(type) },
                    isSelected = type == currentType,
                    productCount = productCount,
                )
            }
        }
    }

    @Composable
    private fun DeliveryTypeTab(
        type: DeliveryType,
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
                DeliveryType.DELIVERY -> R.string.delivery
                DeliveryType.PICK_UP_FROM_STORE -> R.string.from_store
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
                    AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.Center,
                label = "DeliveryTypeButton product count",
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
    private fun DeliveryTypeContentPager(
        pagerState: PagerState,
        deliveryTypes: ImmutableList<DeliveryType>,
        deliveryCartState: State<CartState>,
        pickUpFromStoreCartState: State<CartState>,
        onGoToCatalogClicked: () -> Unit,
        onCartErrorRefreshClicked: () -> Unit,
        productCardActions: ProductCardActions,
        onBonusAccrualClicked: () -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        promoCodeTextFieldState: TextFieldState,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val deliveryType = deliveryTypes[page]
            val cartState = when (deliveryType) {
                DeliveryType.DELIVERY -> deliveryCartState
                DeliveryType.PICK_UP_FROM_STORE -> pickUpFromStoreCartState
            }.value

            Crossfade(
                targetState = cartState,
                contentKey = ::getDeliveryTypePagerContentKey,
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                when (state) {
                    is CartState.Cart -> {
                        Cart(
                            cartState = state,
                            productCardActions = productCardActions,
                            onBonusAccrualClicked = onBonusAccrualClicked,
                            onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                            promoCodeTextFieldState = promoCodeTextFieldState,
                            onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                            onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    CartState.Loading -> {
                        CartSkeleton()
                    }

                    CartState.EmptyCart -> {
                        EmptyCartPlaceholder(
                            deliveryType = deliveryType,
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
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        promoCodeTextFieldState: TextFieldState,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(modifier = modifier) {
            val lazyListState = rememberLazyListState()

            CartList(
                cartState = cartState,
                productCardActions = productCardActions,
                lazyListState = lazyListState,
                onBonusAccrualClicked = onBonusAccrualClicked,
                onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                promoCodeTextFieldState = promoCodeTextFieldState,
                onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                modifier = Modifier.matchParentSize(),
            )

            val isCheckoutBlockVisible by remember {
                derivedStateOf {
                    val visibleItems = lazyListState.layoutInfo.visibleItemsInfo
                    visibleItems.any { it.contentType == CartContentType.CheckoutBlock }
                }
            }

            FloatingCheckoutBlock(
                isVisible = !isCheckoutBlockVisible && !WindowInsets.isImeVisible,
                totalPrice = cartState.price.totalPrice,
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
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        promoCodeTextFieldState: TextFieldState,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
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
                        .animateItem(),
                )
            }

            if (cartState.bonuses.accrualForPurchase != 0) {
                item(
                    key = CartKey.BonusAccrual,
                    contentType = CartContentType.BonusAccrual,
                ) {
                    BonusAccrual(
                        bonusCount = cartState.bonuses.accrualForPurchase,
                        onClick = onBonusAccrualClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 32.dp)
                            .padding(start = 16.dp, end = 8.dp)
                            .animateItem(),
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
                            .animateItem(),
                    )
                }
            }

            item(
                key = CartKey.PromoCode,
                contentType = CartContentType.PromoCode,
            ) {
                ZarinaPromoCodeTextField(
                    state = promoCodeTextFieldState,
                    isApplied = cartState.promoCodeState.isApplied,
                    onApplyClicked = onApplyPromoCodeClicked,
                    onRemoveClicked = onRemovePromoCodeClicked,
                    isError = cartState.promoCodeState.isInvalid,
                    description = {
                        AnimatedContent(
                            targetState = cartState.promoCodeState.description,
                            transitionSpec = {
                                AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                            },
                            contentAlignment = Alignment.Center,
                            label = "PromoCode description",
                        ) { text ->
                            if (text != null) {
                                Text(text = textString(text))
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp)
                        .animateItem(),
                )
            }

            item(
                key = CartKey.Price,
                contentType = CartContentType.Price,
            ) {
                CartPrice(
                    cartPrice = cartState.price.cartPrice,
                    discountSize = cartState.price.discountSize,
                    totalPrice = cartState.price.totalPrice,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .animateItem(),
                )
            }

            item(
                key = CartKey.CheckoutBlock,
                contentType = CartContentType.CheckoutBlock,
            ) {
                ZarinaButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 12.dp, bottom = 20.dp)
                        .animateItem(),
                ) {
                    Text(text = stringResource(R.string.checkout).uppercase())
                }
            }
        }
    }

    @Composable
    private fun FloatingCheckoutBlock(
        isVisible: Boolean,
        totalPrice: Int,
        modifier: Modifier = Modifier,
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
            modifier = modifier,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(UiKitTheme.colors.background.general.regular.default)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            ) {
                Column(Modifier.weight(1f)) {
                    val textColor = UiKitTheme.colors.text.general.regular.default
                    Text(
                        text = stringResource(R.string.total),
                        style = UiKitTheme.typography.tertiary.light,
                        color = textColor,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val formattedPrice = rememberFormattedPrice(totalPrice)
                    Text(
                        text = stringResource(R.string.price_in_rubles_string, formattedPrice),
                        style = UiKitTheme.typography.primary.bold,
                        color = textColor,
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                ZarinaButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = stringResource(R.string.checkout).uppercase())
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
        }
    }

    @Composable
    private fun EmptyCartPlaceholder(
        deliveryType: DeliveryType,
        onGoToCatalogClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val iconResId: Int
        val titleResId: Int
        val bodyResId: Int
        when (deliveryType) {
            DeliveryType.DELIVERY -> {
                iconResId = R.drawable.ic_scooter_64
                titleResId = R.string.cart_empty_delivery_cart_placeholder_title
                bodyResId = R.string.cart_empty_delivery_cart_placeholder_body
            }

            DeliveryType.PICK_UP_FROM_STORE -> {
                iconResId = R.drawable.ic_store_64
                titleResId = R.string.cart_empty_pick_up_from_store_cart_placeholder_title
                bodyResId = R.string.cart_empty_pick_up_from_store_cart_placeholder_body
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
            onButtonClicked = onGoToCatalogClicked,
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun SwipeableProductOrderCard(
        productItem: CartViewModel.ProductItem,
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
                val countStyle = remember(
                    productItem.availableCount,
                    productCardActions.onCountClicked,
                ) {
                    ProductOrderCardCountStyle.Selector(
                        isEditable = productItem.availableCount > 1,
                        onClick = { productCardActions.onCountClicked(product) },
                    )
                }

                ProductOrderCard(
                    name = product.name,
                    imageUrl = product.imageUrl,
                    size = product.size,
                    sizeRu = null,
                    height = product.height,
                    color = product.color,
                    count = product.count,
                    countStyle = countStyle,
                    price = product.price,
                    modifier = Modifier.fillMaxWidth(),
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
                    if (product.isInFavorites) {
                        iconResId = R.drawable.ic_heart_24
                        textRedId = R.string.in_favorites
                    } else {
                        iconResId = R.drawable.ic_heart_outline_24
                        textRedId = R.string.to_favorites
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
                        imageVector = ImageVector.vectorResource(R.drawable.ic_trash_can_24),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.delete).uppercase(),
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
    private fun BonusAccrual(
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
            val baseText = stringResource(R.string.we_will_award_you_for_purchase)
            val bonusText = pluralStringResource(R.plurals.d_bonuses, bonusCount, bonusCount)
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
                        imageVector = ImageVector.vectorResource(R.drawable.ic_question_mark_shaped_24),
                        contentDescription = stringResource(R.string.for_zarina_club_members),
                        modifier = Modifier.size(iconSize),
                    )
                }
            }
        }
    }

    @Composable
    private fun MyCard(
        state: MyCardState,
        onIsAppliedChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.apply_my_card),
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
                onCheckedChanged = { onIsAppliedChanged(it) },
            )
        }
    }

    private fun getDeliveryTypePagerContentKey(cartState: CartState): Any = when (cartState) {
        is CartState.Cart -> DeliveryTypePagerContentKeyCart
        CartState.EmptyCart, is CartState.Error, CartState.Loading -> cartState
    }

    private fun getCartProductItemKey(productItem: CartViewModel.ProductItem): CartKey.Product {
        return CartKey.Product(productItem.product.productId.value)
    }

    @Stable
    class ProductCardActions(
        val onCountClicked: (CartProduct) -> Unit,
        val onAddToFavoritesClicked: (CartProduct) -> Unit,
        val onDeleteFromCartClicked: (CartProduct) -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as ProductCardActions

            if (onCountClicked != other.onCountClicked) return false
            if (onAddToFavoritesClicked != other.onAddToFavoritesClicked) return false
            if (onDeleteFromCartClicked != other.onDeleteFromCartClicked) return false

            return true
        }

        override fun hashCode(): Int {
            var result = onCountClicked.hashCode()
            result = 31 * result + onAddToFavoritesClicked.hashCode()
            result = 31 * result + onDeleteFromCartClicked.hashCode()
            return result
        }
    }

    private const val CartProductSkeletonCount = 6

    private enum class ProductOrderCardSwipeableState { Default, SwipedLeft }

    private const val DeliveryTypePagerContentKeyCart = "DeliveryTypePagerContentKeyCart"

    @Stable
    @Parcelize
    private sealed class CartKey : Parcelable {
        data class Product(val productId: String) : CartKey()

        data object BonusAccrual : CartKey()

        data object MyCard : CartKey()

        data object PromoCode : CartKey()

        data object Price : CartKey()

        data object CheckoutBlock : CartKey()
    }

    private enum class CartContentType {
        Product,
        BonusAccrual,
        MyCard,
        PromoCode,
        Price,
        CheckoutBlock,
    }

    private val ProductOrderCardSwipeDistance: Dp get() = 120.dp
}

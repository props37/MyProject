package ru.zarina.zarina.ui.screen.cart

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.cart.Cart
import ru.zarina.zarina.domain.cart.CartSize
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.base.rememberErrorState
import ru.zarina.zarina.ui.common.component.ProductOrderCard
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.counter.ZarinaCounter
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.zarina.zarina.ui.common.component.tab.ZarinaTabRow
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.screen.cart.CartViewModel.CartState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.animation.Crossfade
import ru.zarina.zarina.util.compose.pager.PagerTabRowIntegration

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
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
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
                    ) {
                        Text(text = stringResource(R.string.clear).uppercase())
                    }
                }
            },
            modifier = modifier,
        )
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun CartContent(
        city: City?,
        onCityClicked: () -> Unit,
        cartSize: CartSize,
        deliveryTypes: ImmutableList<DeliveryType>,
        currentDeliveryType: DeliveryType,
        onDeliveryTypeChanged: (DeliveryType) -> Unit,
        deliveryTypeToCartState: ImmutableMap<DeliveryType, StateFlow<CartState>>,
        onGoToCatalogClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
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
                cartSize = cartSize,
                modifier = Modifier.padding(horizontal = 16.dp),
            )

            DeliveryTypeContentPager(
                pagerState = pagerState,
                deliveryTypes = deliveryTypes,
                deliveryTypeToCartState = deliveryTypeToCartState,
                onGoToCatalogClicked = onGoToCatalogClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @Composable
    private fun City(
        city: City?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = city,
            modifier = modifier
                .clickable(
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
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        painter = painterResource(R.drawable.ic_small_arrow_up_24),
                        contentDescription = stringResource(R.string.change_city),
                        tint = UiKitTheme.colors.icon.regular.default,
                        modifier = Modifier
                            .size(16.dp)
                            .rotate(degrees = 90f),
                    )
                } else {
                    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
                    ZarinaSkeleton(
                        shimmer = shimmer,
                        modifier = Modifier
                            .fillMaxWidth(fraction = 0.55f)
                            .height(16.dp),
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
                    DeliveryType.PICK_UP_FROM_SHOP -> cartSize.pickUpFromShopProductCount
                }
                DeliveryTypeButton(
                    type = type,
                    onClick = { onTypeChanged(type) },
                    isSelected = type == currentType,
                    productCount = productCount,
                )
            }
        }
    }

    @Composable
    private fun DeliveryTypeButton(
        type: DeliveryType,
        onClick: () -> Unit,
        isSelected: Boolean,
        productCount: Int,
        modifier: Modifier = Modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.backlessColors(),
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
            modifier = modifier,
        ) {
            val textResId = when (type) {
                DeliveryType.DELIVERY -> R.string.delivery
                DeliveryType.PICK_UP_FROM_SHOP -> R.string.from_shop
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

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    private fun DeliveryTypeContentPager(
        pagerState: PagerState,
        deliveryTypes: ImmutableList<DeliveryType>,
        deliveryTypeToCartState: ImmutableMap<DeliveryType, StateFlow<CartState>>,
        onGoToCatalogClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            val deliveryType = deliveryTypes[page]
            val cartState = deliveryTypeToCartState[deliveryType]
                ?.collectAsStateWithLifecycle()?.value
                ?: CartState.InitialLoading

            Crossfade(
                targetState = cartState,
                contentKey = ::getDeliveryTypePagerContentKey,
                modifier = Modifier.fillMaxSize(),
            ) { state ->
                when (state) {
                    is CartState.Cart -> {
                        Cart(
                            cart = state.cart,
                            modifier = Modifier.fillMaxSize(),
                        )
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

                    is CartState.Error -> {
                        ZarinaErrorScreen(
                            state = state.state,
                            onRefreshClicked = { /*TODO*/ },
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun Cart(
        cart: Cart,
        modifier: Modifier = Modifier,
    ) {
        LazyColumn(modifier = modifier) {
            // TODO: [High] Implement contentType
            itemsIndexed(
                items = cart.products,
                key = { _, product -> product.id.value },
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

                if (index < cart.products.lastIndex) {
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

    private fun getDeliveryTypePagerContentKey(cartState: CartState): Any = when (cartState) {
        is CartState.Cart -> DeliveryTypePagerContentKeyCart
        CartState.EmptyCart -> cartState
        is CartState.Error -> cartState
        CartState.InitialLoading -> cartState
    }

    private const val DeliveryTypePagerContentKeyCart = "DeliveryTypePagerContentKeyCart"
}

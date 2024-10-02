package ru.livetyping.zarina.presentation.screen.checkout.orderplacing

import android.os.Parcelable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.checkout.Customer
import ru.livetyping.zarina.domain.checkout.PaymentMethod
import ru.livetyping.zarina.presentation.base.text.textString
import ru.livetyping.zarina.presentation.common.animation.LazyListFadeInSpec
import ru.livetyping.zarina.presentation.common.animation.LazyListFadeOutSpec
import ru.livetyping.zarina.presentation.common.animation.LazyListPlacementSpec
import ru.livetyping.zarina.presentation.common.component.CartPrice
import ru.livetyping.zarina.presentation.common.component.ProductOrderCard
import ru.livetyping.zarina.presentation.common.component.ProductOrderCardCountStyle
import ru.livetyping.zarina.presentation.common.component.ProductOrderCardSkeleton
import ru.livetyping.zarina.presentation.common.component.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.presentation.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.presentation.common.component.button.ZarinaCloseIconButton
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.item.ZarinaItem
import ru.livetyping.zarina.presentation.common.component.loader.ZarinaCircularLoader
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.selector.ZarinaButtonSelector
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaPromoCodeTextField
import ru.livetyping.zarina.presentation.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.util.domain.nameResId
import ru.livetyping.zarina.presentation.common.util.rememberFormattedPhoneNumber
import ru.livetyping.zarina.presentation.screen.cart.CartScreenComponents
import ru.livetyping.zarina.presentation.screen.cart.model.CartState
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.DeliveryInfo
import ru.livetyping.zarina.presentation.screen.checkout.orderplacing.CheckoutOrderPlacingViewModel.PaymentMethodsState
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Suppress("ConstPropertyName")
object CheckoutOrderPlacingScreenComponents {

    @Composable
    fun OrderPlacing(
        customer: Customer,
        onChangeCustomerClicked: () -> Unit,
        deliveryInfo: DeliveryInfo,
        onChangeDeliveryClicked: () -> Unit,
        cartState: CartState,
        onCartErrorRefreshClicked: () -> Unit,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onBonusAccrualClicked: () -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        selectedPaymentMethod: PaymentMethod?,
        onPaymentMethodSelectorClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = navigationBarHeight + 20.dp)

        LazyColumn(
            contentPadding = contentPadding,
            modifier = modifier,
        ) {
            item(
                key = OrderPlacingKey.Customer,
                contentType = OrderPlacingContentType.Customer,
            ) {
                Customer(
                    customer = customer,
                    onChangeClicked = onChangeCustomerClicked,
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
                )
            }

            item(
                key = OrderPlacingKey.DeliveryInfo,
                contentType = OrderPlacingContentType.DeliveryInfo,
            ) {
                DeliveryInfo(
                    deliveryInfo = deliveryInfo,
                    onChangeClicked = onChangeDeliveryClicked,
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
                )
            }

            item(
                key = OrderPlacingKey.YourOrder,
                contentType = OrderPlacingContentType.YourOrder,
            ) {
                YourOrder(
                    modifier = Modifier.animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
                )
            }

            cartItems(
                cartState = cartState,
                onCartErrorRefreshClicked = onCartErrorRefreshClicked,
                onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                onBonusAccrualClicked = onBonusAccrualClicked,
                onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                selectedPaymentMethod = selectedPaymentMethod,
                onPaymentMethodSelectorClicked = onPaymentMethodSelectorClicked,
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaymentMethodsBottomSheet(
        isVisible: Boolean,
        onDismissRequest: () -> Unit,
        paymentMethodsState: PaymentMethodsState,
        onPaymentMethodSelected: (PaymentMethod) -> Unit,
        onPaymentMethodsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
        sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        val coroutineScope = rememberCoroutineScope()

        fun closeBottomSheet() {
            coroutineScope
                .launch { sheetState.hide() }
                .invokeOnCompletion { onDismissRequest() }
        }

        if (isVisible) {
            ZarinaModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                modifier = modifier,
            ) {
                PaymentMethodsBottomSheetTopBar(
                    onCloseClicked = { closeBottomSheet() },
                )

                PaymentMethodsBottomSheetContent(
                    paymentMethodsState = paymentMethodsState,
                    onPaymentMethodSelected = {
                        onPaymentMethodSelected(it)
                        closeBottomSheet()
                    },
                    onPaymentMethodsErrorRefreshClicked = onPaymentMethodsErrorRefreshClicked,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }

    @Composable
    private fun Customer(
        customer: Customer,
        onChangeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                startContent = {
                    Text(
                        text = stringResource(R.string.customer),
                        style = UiKitTheme.typography.secondary.bold,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                },
                endContent = {
                    OrderListHeaderChangeButton(onClick = onChangeClicked)
                },
                contentPadding = OrderPlacingListHeaderTitleContentPadding,
            )

            ZarinaItem(contentPadding = OrderPlacingListHeaderDescriptionContentPadding) {
                Column {
                    val fullName = remember(customer) { customer.getFullName() }
                    Text(
                        text = fullName,
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val formattedPhone = rememberFormattedPhoneNumber(customer.phone.value)
                    val contacts = remember(customer, formattedPhone) {
                        buildString {
                            append(customer.email.value)
                            if (formattedPhone != null) {
                                append(CommaSeparator)
                                append(formattedPhone)
                            }
                        }
                    }
                    Text(
                        text = contacts,
                        style = UiKitTheme.typography.tertiary.light,
                        color = UiKitTheme.colors.text.general.regular.muted,
                    )
                }
            }
        }
    }

    @Composable
    private fun DeliveryInfo(
        deliveryInfo: DeliveryInfo,
        onChangeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                startContent = {
                    Text(
                        text = stringResource(R.string.delivery_method),
                        style = UiKitTheme.typography.secondary.bold,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )
                },
                endContent = {
                    OrderListHeaderChangeButton(onClick = onChangeClicked)
                },
                contentPadding = OrderPlacingListHeaderTitleContentPadding,
            )

            ZarinaItem(contentPadding = OrderPlacingListHeaderDescriptionContentPadding) {
                Column {
                    Text(
                        text = stringResource(deliveryInfo.deliveryMethodType.nameResId),
                        style = UiKitTheme.typography.secondary.light,
                        color = UiKitTheme.colors.text.general.regular.default,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    deliveryInfo.descriptions.forEachIndexed { index, description ->
                        Text(
                            text = description,
                            style = UiKitTheme.typography.tertiary.light,
                            color = UiKitTheme.colors.text.general.regular.muted,
                        )

                        if (index < deliveryInfo.descriptions.lastIndex) {
                            Spacer(modifier = Modifier.height(2.dp))
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun YourOrder(
        modifier: Modifier = Modifier,
    ) {
        ZarinaItem(modifier = modifier) {
            Text(
                text = stringResource(R.string.your_order),
                style = UiKitTheme.typography.secondary.bold,
            )
        }
    }

    private fun LazyListScope.cartItems(
        cartState: CartState,
        onCartErrorRefreshClicked: () -> Unit,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onBonusAccrualClicked: () -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        selectedPaymentMethod: PaymentMethod?,
        onPaymentMethodSelectorClicked: () -> Unit,
    ) {
        when (cartState) {
            is CartState.Cart -> {
                cartItemsImpl(
                    cartState = cartState,
                    onIsBonusWriteOffAppliedChanged = onIsBonusWriteOffAppliedChanged,
                    onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                    onBonusAccrualClicked = onBonusAccrualClicked,
                    onIsMyCardAppliedChanged = onIsMyCardAppliedChanged,
                    onApplyPromoCodeClicked = onApplyPromoCodeClicked,
                    onRemovePromoCodeClicked = onRemovePromoCodeClicked,
                    onPromoCodeImeDoneClicked = onPromoCodeImeDoneClicked,
                    selectedPaymentMethod = selectedPaymentMethod,
                    onPaymentMethodSelectorClicked = onPaymentMethodSelectorClicked,
                )
            }

            CartState.Loading -> {
                item(
                    key = OrderPlacingKey.CartSkeleton,
                    contentType = OrderPlacingContentType.CartSkeleton,
                ) {
                    CartSkeleton(
                        modifier = Modifier.animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                    )
                }
            }

            is CartState.Error -> {
                item(
                    key = OrderPlacingKey.CartError,
                    contentType = OrderPlacingContentType.CartError,
                ) {
                    CartError(
                        cartState = cartState,
                        onRefreshClicked = onCartErrorRefreshClicked,
                        modifier = Modifier.animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                    )
                }
            }

            CartState.EmptyCart -> {
                item(
                    key = OrderPlacingKey.EmptyCartError,
                    contentType = OrderPlacingContentType.EmptyCartError,
                ) {
                    EmptyCartError(
                        onRefreshClicked = onCartErrorRefreshClicked,
                        modifier = Modifier.animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                    )
                }
            }
        }
    }

    private fun LazyListScope.cartItemsImpl(
        cartState: CartState.Cart,
        onIsBonusWriteOffAppliedChanged: (Boolean) -> Unit,
        onBonusCountToWriteOffChanged: (Int?) -> Unit,
        onBonusAccrualClicked: () -> Unit,
        onIsMyCardAppliedChanged: (Boolean) -> Unit,
        onApplyPromoCodeClicked: () -> Unit,
        onRemovePromoCodeClicked: () -> Unit,
        onPromoCodeImeDoneClicked: () -> Unit,
        selectedPaymentMethod: PaymentMethod?,
        onPaymentMethodSelectorClicked: () -> Unit,
    ) {
        itemsIndexed(
            items = cartState.productItems,
            key = { _, productItem ->
                OrderPlacingKey.CartProduct(productItem.product.id.value)
            },
            contentType = { _, _ -> OrderPlacingContentType.CartProduct },
        ) { index, productItem ->
            Column(
                modifier = Modifier.animateItem(
                    fadeInSpec = LazyListFadeInSpec,
                    placementSpec = LazyListPlacementSpec,
                    fadeOutSpec = LazyListFadeInSpec,
                ),
            ) {
                val product = productItem.product
                val countStyle = remember {
                    ProductOrderCardCountStyle.Selector(isEditable = false, onClick = null)
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

                if (index < cartState.productItems.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }

        item(
            key = OrderPlacingKey.PaymentMethod,
            contentType = OrderPlacingContentType.PaymentMethod,
        ) {
            PaymentMethodSelector(
                selectedPaymentMethod = selectedPaymentMethod,
                onClick = onPaymentMethodSelectorClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp)
                    .animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeInSpec,
                    ),
            )
        }

        if (cartState.bonusState.bonuses.accrualForPurchase != 0) {
            item(
                key = OrderPlacingKey.BonusAccrual,
                contentType = OrderPlacingContentType.BonusAccrual,
            ) {
                CartScreenComponents.BonusAccrual(
                    bonusCount = cartState.bonusState.bonuses.accrualForPurchase,
                    onClick = onBonusAccrualClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(start = 16.dp, end = 8.dp)
                        .animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                )
            }
        }

        if (cartState.bonusState.isWriteOffAvailable) {
            item(
                key = OrderPlacingKey.BonusWriteOff,
                contentType = OrderPlacingContentType.BonusWriteOff,
            ) {
                CartScreenComponents.BonusWriteOff(
                    state = cartState.bonusState,
                    onIsAppliedChanged = onIsBonusWriteOffAppliedChanged,
                    onBonusCountToWriteOffChanged = onBonusCountToWriteOffChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(start = 16.dp, end = 8.dp)
                        .animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                )
            }
        }

        if (cartState.myCardState != null) {
            item(
                key = OrderPlacingKey.MyCard,
                contentType = OrderPlacingContentType.MyCard,
            ) {
                CartScreenComponents.MyCard(
                    state = cartState.myCardState,
                    onIsAppliedChanged = onIsMyCardAppliedChanged,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(start = 16.dp, end = 8.dp)
                        .animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeInSpec,
                        ),
                )
            }
        }

        if (cartState.promoCodeState != null) {
            item(
                key = OrderPlacingKey.PromoCode,
                contentType = OrderPlacingContentType.PromoCode,
            ) {
                ZarinaPromoCodeTextField(
                    state = cartState.promoCodeState.textFieldState,
                    isApplied = cartState.promoCodeState.isApplied,
                    appliedPromoCode = cartState.promoCodeState.appliedPromoCode,
                    onApplyClicked = onApplyPromoCodeClicked,
                    onRemoveClicked = onRemovePromoCodeClicked,
                    isError = cartState.promoCodeState.isInvalid,
                    description = {
                        CartScreenComponents.PromoCodeDescription(
                            text = cartState.promoCodeState.description?.let { textString(it) },
                        )
                    },
                    onKeyboardAction = { onPromoCodeImeDoneClicked() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(horizontal = 16.dp)
                        .animateItem(
                            fadeInSpec = LazyListFadeInSpec,
                            placementSpec = LazyListPlacementSpec,
                            fadeOutSpec = LazyListFadeOutSpec,
                        ),
                )
            }
        }

        item(
            key = OrderPlacingKey.Price,
            contentType = OrderPlacingContentType.Price,
        ) {
            CartPrice(
                cartPrice = cartState.price.cartPrice,
                discountSize = cartState.price.discountSize,
                totalPrice = cartState.price.totalPrice,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .animateItem(
                        fadeInSpec = LazyListFadeInSpec,
                        placementSpec = LazyListPlacementSpec,
                        fadeOutSpec = LazyListFadeOutSpec,
                    ),
            )
        }
    }

    @Composable
    private fun CartSkeleton(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

            repeat(CartSkeletonItemCount) { index ->
                ProductOrderCardSkeleton(shimmer = shimmer)

                if (index < CartSkeletonItemCount - 1) {
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
    private fun CartError(
        cartState: CartState.Error,
        onRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaErrorScreen(
            state = cartState.state,
            onButtonClicked = onRefreshClicked,
            modifier = modifier
                .fillMaxWidth()
                .padding(CartErrorPadding),
        )
    }

    @Composable
    private fun EmptyCartError(
        onRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val errorState = remember { ErrorState.GENERIC }
        ZarinaErrorScreen(
            state = errorState,
            onButtonClicked = onRefreshClicked,
            modifier = modifier
                .fillMaxWidth()
                .padding(CartErrorPadding),
        )
    }

    @Composable
    private fun PaymentMethodSelector(
        selectedPaymentMethod: PaymentMethod?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val label = @Composable {
            val text = selectedPaymentMethod?.let {
                stringResource(R.string.payment_method)
            }.orEmpty()
            Text(text = text)
        }

        val content: (@Composable () -> Unit)? = selectedPaymentMethod?.let {
            @Composable {
                Text(text = selectedPaymentMethod.title)
            }
        }

        ZarinaButtonSelector(
            onClick = onClick,
            placeholder = {
                Text(
                    text = stringResource(R.string.payment_method),
                    maxLines = 1,
                )
            },
            label = label,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            applyContentPaddingToDivider = true,
            content = content,
            modifier = modifier,
        )
    }

    @Composable
    private fun PaymentMethodsBottomSheetTopBar(
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(text = stringResource(R.string.select_payment_method))
            },
            endContent = {
                ZarinaCloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = 4.dp),
            modifier = modifier,
        )
    }

    @Composable
    private fun PaymentMethodsBottomSheetContent(
        paymentMethodsState: PaymentMethodsState,
        onPaymentMethodSelected: (PaymentMethod) -> Unit,
        onPaymentMethodsErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        AnimatedContent(
            targetState = paymentMethodsState,
            transitionSpec = { fadeIn() togetherWith  fadeOut() },
            contentKey = {
                when (it) {
                    is PaymentMethodsState.Success -> PaymentMethodsBottomSheetContentKeySuccess
                    PaymentMethodsState.Loading -> it
                    is PaymentMethodsState.Error -> it
                }
            },
            label = "PaymentMethodsBottomSheetContent",
            modifier = modifier,
        ) { state ->
            when (state) {
                is PaymentMethodsState.Success -> {
                    PaymentMethodsBottomSheetContentSuccess(
                        paymentMethodsState = state,
                        onPaymentMethodSelected = onPaymentMethodSelected,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                PaymentMethodsState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 56.dp),
                    ) {
                        ZarinaCircularLoader(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(40.dp),
                        )
                    }
                }

                is PaymentMethodsState.Error -> {
                    ZarinaErrorScreen(
                        state = state.errorState,
                        onButtonClicked = onPaymentMethodsErrorRefreshClicked,
                        fillWholeHeight = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 48.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun PaymentMethodsBottomSheetContentSuccess(
        paymentMethodsState: PaymentMethodsState.Success,
        onPaymentMethodSelected: (PaymentMethod) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier.verticalScroll(rememberScrollState())) {
            paymentMethodsState.paymentMethods.forEachIndexed { index, paymentMethod ->
                key(paymentMethod.id.value) {
                    ZarinaItem(
                        onClick = { onPaymentMethodSelected(paymentMethod) },
                    ) {
                        Column {
                            Text(
                                text = paymentMethod.title,
                                style = UiKitTheme.typography.secondary.light,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = paymentMethod.description,
                                style = UiKitTheme.typography.footnote.light,
                                color = UiKitTheme.colors.text.general.regular.muted,
                            )
                        }
                    }

                    if (index < paymentMethodsState.paymentMethods.lastIndex) {
                        ZarinaDivider(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                        )
                    }
                }
            }
        }
    }

    @Composable
    private fun OrderListHeaderChangeButton(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.backlessColors(),
            modifier = modifier,
        ) {
            Text(text = stringResource(R.string.change).uppercase())
        }
    }

    @Parcelize
    @Stable
    private sealed class OrderPlacingKey : Parcelable {
        data object Customer : OrderPlacingKey()

        data object DeliveryInfo : OrderPlacingKey()

        data object YourOrder : OrderPlacingKey()

        data object CartError : OrderPlacingKey()

        data object EmptyCartError : OrderPlacingKey()

        data object CartSkeleton : OrderPlacingKey()

        data class CartProduct(val id: Long) : OrderPlacingKey()

        data object PaymentMethod : OrderPlacingKey()

        data object BonusAccrual : OrderPlacingKey()

        data object BonusWriteOff : OrderPlacingKey()

        data object MyCard : OrderPlacingKey()

        data object PromoCode : OrderPlacingKey()

        data object Price : OrderPlacingKey()
    }

    @Stable
    private enum class OrderPlacingContentType {
        Customer,
        DeliveryInfo,
        YourOrder,
        CartError,
        EmptyCartError,
        CartSkeleton,
        CartProduct,
        PaymentMethod,
        BonusAccrual,
        BonusWriteOff,
        MyCard,
        PromoCode,
        Price,
    }

    @Stable
    private val OrderPlacingListHeaderTitleContentPadding: PaddingValues
        get() = PaddingValues(start = 16.dp, top = 4.dp, end = 8.dp, bottom = 4.dp)

    @Stable
    private val OrderPlacingListHeaderDescriptionContentPadding: PaddingValues
        get() = PaddingValues(start = 16.dp, top = 0.dp, end = 16.dp, bottom = 8.dp)

    private const val CommaSeparator = ", "

    private const val CartSkeletonItemCount = 4

    private val CartErrorPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 56.dp)

    private const val PaymentMethodsBottomSheetContentKeySuccess =
        "PaymentMethodsBottomSheetContentKeySuccess"
}

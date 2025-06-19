package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.ui

import android.os.Parcelable
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.domain.model.checkout.PaymentMethod
import ru.livetyping.zarina.core.domain.model.checkout.Recipient
import ru.livetyping.zarina.core.uicommon.nameResId
import ru.livetyping.zarina.core.uicompose.rememberFormattedPhoneNumber
import ru.livetyping.zarina.core.uicompose.text.rememberAnnotatedStringWithLinks
import ru.livetyping.zarina.core.uicompose.text.textString
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaModalBottomSheet
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSelector
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.cart.CartPrice
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uikit.item.ZarinaItem
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.product.ProductOrderCard
import ru.livetyping.zarina.core.uikit.product.ProductOrderCardCountStyle
import ru.livetyping.zarina.core.uikit.product.ProductOrderCardSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.text.ZarinaPromoCodeTextField
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.model.CartState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.cart.ui.CartScreenComponents
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.DeliveryInfo
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.InfoButton
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.InfoModalBottomSheetState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.orderplacing.OrderPlacingViewModel.PaymentMethodsState
import ru.livetyping.zarina.core.resource.R as RCommon

@Suppress("ConstPropertyName")
internal object OrderPlacingScreenComponents {

    @OptIn(ExperimentalLayoutApi::class)
    @Composable
    fun OrderPlacing(
        recipient: Recipient,
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
        onPayClicked: () -> Unit,
        isPayButtonLoading: Boolean,
        onInfoButtonClicked: (InfoButton) -> Unit,
        onUrlClicked: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val listState = rememberLazyListState()
        val navigationBarHeight =
            WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val contentPadding = PaddingValues(bottom = navigationBarHeight + 20.dp)

        Box(modifier = modifier) {
            LazyColumn(
                state = listState,
                contentPadding = contentPadding,
            ) {
                item(
                    key = OrderPlacingKey.Customer,
                    contentType = OrderPlacingContentType.Customer,
                ) {
                    Customer(
                        recipient = recipient,
                        onChangeClicked = onChangeCustomerClicked,
                        modifier = Modifier.animateZarinaItem(this),
                    )
                }

                item(
                    key = OrderPlacingKey.DeliveryInfo,
                    contentType = OrderPlacingContentType.DeliveryInfo,
                ) {
                    DeliveryInfo(
                        deliveryInfo = deliveryInfo,
                        onChangeClicked = onChangeDeliveryClicked,
                        modifier = Modifier.animateZarinaItem(this),
                    )
                }

                item(
                    key = OrderPlacingKey.YourOrder,
                    contentType = OrderPlacingContentType.YourOrder,
                ) {
                    YourOrder(
                        modifier = Modifier.animateZarinaItem(this),
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
                    onPayClicked = onPayClicked,
                    isPayButtonLoading = isPayButtonLoading,
                    onInfoButtonClicked = onInfoButtonClicked,
                    onUrlClicked = onUrlClicked,
                )
            }

            if (cartState is CartState.Cart) {
                val isPayItemVisible by remember {
                    derivedStateOf {
                        val visibleItems = listState.layoutInfo.visibleItemsInfo
                        visibleItems.any { it.contentType == OrderPlacingContentType.Pay }
                    }
                }

                CartScreenComponents.CartBottomFloatingBlock(
                    isVisible = !isPayItemVisible && !WindowInsets.isImeVisible,
                    finalPrice = cartState.price.finalPrice,
                    buttonText = stringResource(RCommon.string.res_pay).uppercase(),
                    isButtonEnabled = true,
                    onButtonClicked = onPayClicked,
                    isButtonLoading = isPayButtonLoading,
                    windowInsets = WindowInsets.navigationBars,
                    modifier = Modifier.align(Alignment.BottomCenter),
                )
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun InfoModalBottomSheet(
        state: InfoModalBottomSheetState?,
        onDismissRequest: () -> Unit,
        modifier: Modifier = Modifier,
        sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        if (state != null) {
            val coroutineScope = rememberCoroutineScope()

            ZarinaModalBottomSheet(
                onDismissRequest = onDismissRequest,
                sheetState = sheetState,
                modifier = modifier,
            ) {
                ZarinaTopBar(
                    endContent = {
                        ZarinaCloseIconButton(
                            onClick = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    onDismissRequest()
                                }
                            },
                            iconSize = 20.dp,
                            modifier = Modifier.padding(end = 2.dp),
                        )
                    },
                    contentPadding = PaddingValues(vertical = 4.dp),
                )

                Text(
                    text = textString(state.text).uppercase(),
                    style = UiKitTheme2.typography.body,
                    color = UiKitTheme2.colors.mainBlack,
                    modifier = Modifier.padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun PaymentMethodSelectorBottomSheet(
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
        recipient: Recipient,
        onChangeClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            ZarinaItem(
                startContent = {
                    Text(
                        text = stringResource(R.string.cart_recipient).uppercase(),
                        style = UiKitTheme2.typography.body,
                        color = UiKitTheme2.colors.mainBlack,
                    )
                },
                endContent = {
                    OrderListHeaderChangeButton(onClick = onChangeClicked)
                },
                contentPadding = OrderPlacingListHeaderTitleContentPadding,
            )

            ZarinaItem(contentPadding = OrderPlacingListHeaderDescriptionContentPadding) {
                Column {
                    val fullName = remember(recipient) { recipient.getFullName() }
                    Text(
                        text = fullName.uppercase(),
                        style = UiKitTheme2.typography.body,
                        color = UiKitTheme2.colors.mainBlack,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val formattedPhone = rememberFormattedPhoneNumber(recipient.phone.value)
                    val contacts = remember(recipient, formattedPhone) {
                        buildString {
                            append(recipient.email.value)
                            if (formattedPhone != null) {
                                append(CommaSeparator)
                                append(formattedPhone)
                            }
                        }
                    }

                    Text(
                        text = contacts.uppercase(),
                        style = UiKitTheme2.typography.body2,
                        color = UiKitTheme2.colors.middleGray,
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
                        text = stringResource(RCommon.string.res_delivery_method).uppercase(),
                        style = UiKitTheme2.typography.bodyBold,
                        color = UiKitTheme2.colors.mainBlack,
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
                        text = stringResource(deliveryInfo.deliveryMethodType.nameResId).uppercase(),
                        style = UiKitTheme2.typography.body,
                        color = UiKitTheme2.colors.mainBlack,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    deliveryInfo.descriptions.forEachIndexed { index, description ->
                        Text(
                            text = description.uppercase(),
                            style = UiKitTheme2.typography.body2,
                            color = UiKitTheme2.colors.middleGray,
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
                text = stringResource(R.string.cart_your_order).uppercase(),
                style = UiKitTheme2.typography.body,
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
        onPayClicked: () -> Unit,
        isPayButtonLoading: Boolean,
        onInfoButtonClicked: (InfoButton) -> Unit,
        onUrlClicked: (String) -> Unit,
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
                    onPayClicked = onPayClicked,
                    isPayButtonLoading = isPayButtonLoading,
                    onInfoButtonClicked = onInfoButtonClicked,
                    onUrlClicked = onUrlClicked,
                )
            }

            CartState.Loading -> {
                item(
                    key = OrderPlacingKey.CartSkeleton,
                    contentType = OrderPlacingContentType.CartSkeleton,
                ) {
                    CartSkeleton(
                        modifier = Modifier.animateZarinaItem(this),
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
                        modifier = Modifier.animateZarinaItem(this),
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
                        modifier = Modifier.animateZarinaItem(this),
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
        onPayClicked: () -> Unit,
        isPayButtonLoading: Boolean,
        onInfoButtonClicked: (InfoButton) -> Unit,
        onUrlClicked: (String) -> Unit,
    ) {
        itemsIndexed(
            items = cartState.productItems,
            key = { _, productItem ->
                OrderPlacingKey.CartProduct(productItem.product.id.value)
            },
            contentType = { _, _ -> OrderPlacingContentType.CartProduct },
        ) { index, productItem ->
            Column(
                modifier = Modifier.animateZarinaItem(this),
            ) {
                val product = productItem.product
                val countStyle = remember {
                    ProductOrderCardCountStyle.Selector(isEditable = false, onClick = null)
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
                    .animateZarinaItem(this),
            )
        }

        if (cartState.bonusAccountState.bonusAccount.addForPurchase != 0) {
            item(
                key = OrderPlacingKey.BonusAccrual,
                contentType = OrderPlacingContentType.BonusAccrual,
            ) {
                CartScreenComponents.BonusAccrual(
                    bonusCount = cartState.bonusAccountState.bonusAccount.addForPurchase,
                    onClick = onBonusAccrualClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                        .padding(start = 16.dp, end = 8.dp)
                        .animateZarinaItem(this),
                )
            }
        }

        if (cartState.bonusAccountState.isRedemptionAvailable) {
            item(
                key = OrderPlacingKey.BonusWriteOff,
                contentType = OrderPlacingContentType.BonusWriteOff,
            ) {
                CartScreenComponents.BonusRedemption(
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
                        .animateZarinaItem(this),
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
                        .animateZarinaItem(this),
                )
            }
        }

        item(
            key = OrderPlacingKey.Price,
            contentType = OrderPlacingContentType.Price,
        ) {
            val isGiftCertificateApplied = cartState.price.giftCertificateRedemptionValue != null
            CartPrice(
                cartPrice = cartState.price.cartPrice,
                discountSize = cartState.price.discountSize,
                isDeliveryPriceIncluded = true,
                deliveryPrice = cartState.price.deliveryPrice,
                giftCertificateWriteOffSize = cartState.price.giftCertificateRedemptionValue,
                isGiftCertificateWriteOffSizeButtonVisible = isGiftCertificateApplied,
                onGiftCertificateWriteOffSizeButtonClicked = {
                    onInfoButtonClicked(InfoButton.GIFT_CERTIFICATE_WRITE_OFF_SIZE)
                },
                finalPrice = cartState.price.finalPrice,
                isFinalPriceDetailsButtonVisible = isGiftCertificateApplied,
                onFinalPriceDetailsButtonClicked = {
                    onInfoButtonClicked(InfoButton.FINAL_PRICE)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .animateZarinaItem(this),
            )
        }

        item(
            key = OrderPlacingKey.Pay,
            contentType = OrderPlacingContentType.Pay,
        ) {
            ZarinaButton(
                onClick = onPayClicked,
                isLoading = isPayButtonLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .animateZarinaItem(this),
            ) {
                Text(text = stringResource(RCommon.string.res_pay).uppercase())
            }
        }

        item(
            key = OrderPlacingKey.PaymentPolicies,
            contentType = OrderPlacingContentType.PaymentPolicies,
        ) {
            PaymentPolicies(
                onUrlClicked = onUrlClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .animateZarinaItem(this),
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
        val errorState = remember { ZarinaErrorScreenState.GENERIC }
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
                stringResource(RCommon.string.res_payment_method)
            }.orEmpty()
            Text(text = text.uppercase())
        }

        val content: (@Composable () -> Unit)? = selectedPaymentMethod?.let {
            @Composable {
                Text(
                    text = selectedPaymentMethod.title.uppercase(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }

        ZarinaButtonSelector(
            onClick = onClick,
            placeholder = {
                Text(
                    text = stringResource(RCommon.string.res_payment_method).uppercase(),
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
                Text(text = stringResource(R.string.cart_select_payment_method).uppercase())
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
            transitionSpec = { fadeIn() togetherWith fadeOut() },
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
                                text = paymentMethod.title.uppercase(),
                                style = UiKitTheme2.typography.body,
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = paymentMethod.description.uppercase(),
                                style = UiKitTheme2.typography.body,
                                color = UiKitTheme2.colors.middleGray,
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
            Text(text = stringResource(RCommon.string.res_change).uppercase())
        }
    }

    @Composable
    private fun PaymentPolicies(
        onUrlClicked: (String) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val policiesRawText = stringResource(R.string.cart_payment_policies)
        val onlineStorePolicy = stringResource(R.string.cart_payment_policies_online_store)
        val privacyPolicy = stringResource(R.string.cart_payment_policies_privacy)
        val onlineStorePolicyUrl = stringResource(RCommon.string.res_zarina_online_store_policy_url)
        val privacyPolicyUrl = stringResource(RCommon.string.res_zarina_privacy_policy_url)

        val substringToUrl = remember(
            onlineStorePolicy,
            onlineStorePolicyUrl,
            privacyPolicy,
            privacyPolicyUrl,
        ) {
            mapOf(
                onlineStorePolicy to onlineStorePolicyUrl,
                privacyPolicy to privacyPolicyUrl,
            )
        }
        val text = rememberAnnotatedStringWithLinks(
            baseString = policiesRawText,
            substringToUrl = substringToUrl,
            linkStyle = UiKitTheme2.typography.body2.toSpanStyle()
                .copy(textDecoration = TextDecoration.Underline),
            onUrlClicked = onUrlClicked,
        )

        Text(
            text = text.toUpperCase(),
            style = UiKitTheme2.typography.body2,
            color = UiKitTheme2.colors.mainBlack,
            modifier = modifier,
        )
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

        data class CartProduct(val id: String) : OrderPlacingKey()

        data object PaymentMethod : OrderPlacingKey()

        data object BonusAccrual : OrderPlacingKey()

        data object BonusWriteOff : OrderPlacingKey()

        data object MyCard : OrderPlacingKey()

        data object PromoCode : OrderPlacingKey()

        data object Price : OrderPlacingKey()

        data object Pay : OrderPlacingKey()

        data object PaymentPolicies : OrderPlacingKey()
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
        Pay,
        PaymentPolicies,
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

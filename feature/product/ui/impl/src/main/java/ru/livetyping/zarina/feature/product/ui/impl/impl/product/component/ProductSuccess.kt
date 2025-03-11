package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import android.os.Parcelable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaLikeIconButton
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductSuggestionsEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductSuggestionsState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ProductSuccess(
    productState: ProductState.Success,
    onProductEvent: (ProductEvent) -> Unit,
    onProductSuggestionsEvent: (ProductSuggestionsEvent) -> Unit,
    onShowZarinaClubDescription: () -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        ProductList(
            productState = productState,
            onProductEvent = onProductEvent,
            onProductSuggestionsEvent = onProductSuggestionsEvent,
            onShowZarinaClubDescription = onShowZarinaClubDescription,
            lazyListState = lazyListState,
            modifier = Modifier.weight(1f),
        )

        val product = productState.product
        BottomBar(
            isProductAvailable = product.isAvailable,
            isProductInCart = product.isInCart,
            isProductInWishlist = product.isInWishlist,
            onAddProductToCartClicked = {
                onProductEvent(ProductEvent.AddToCartClicked(product))
            },
            onAddProductToWishlistClicked = {
                onProductEvent(ProductEvent.AddToWishlistClicked(product))
            },
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ProductList(
    productState: ProductState.Success,
    onProductEvent: (ProductEvent) -> Unit,
    onProductSuggestionsEvent: (ProductSuggestionsEvent) -> Unit,
    onShowZarinaClubDescription: () -> Unit,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val product = productState.product

    LazyColumn(
        state = lazyListState,
        modifier = modifier,
    ) {
        item(
            key = ProductListKey.MediaPager,
            contentType = ProductListContentType.MediaPager,
        ) {
            ProductMediaPager(
                media = product.media,
                modifier = Modifier.animateZarinaItem(this),
            )
        }

        item(
            key = ProductListKey.GeneralInfo,
            contentType = ProductListContentType.GeneralInfo,
        ) {
            ProductGeneralInfo(
                product = product,
                onProductColorClicked = { onProductEvent(ProductEvent.ProductColorClicked(it)) },
                onBonusAccrualForPurchaseClicked = onShowZarinaClubDescription,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 8.dp)
                    .animateZarinaItem(this),
            )
        }

        if (productState.isCheckAvailabilityInStoresButtonVisible) {
            item(
                key = ProductListKey.CheckAvailabilityInStoresButton,
                contentType = ProductListContentType.CheckAvailabilityInStoresButton,
            ) {
                ZarinaButton(
                    onClick = { onProductEvent(ProductEvent.CheckAvailabilityInStoresClicked) },
                    colors = ZarinaButtonDefaults.outlineColors(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 16.dp)
                        .padding(horizontal = 16.dp)
                        .animateZarinaItem(this),
                ) {
                    Text(
                        text = stringResource(RCommon.string.res_availability_in_stores).uppercase(),
                    )
                }
            }
        }

        item(
            key = ProductListKey.Description,
            contentType = ProductListContentType.Description,
        ) {
            ProductDescription(
                description = product.description,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateZarinaItem(this),
            )
        }

        item(
            key = ProductListKey.DeliveryAndPayment,
            contentType = ProductListContentType.DeliveryAndPayment,
        ) {
            ProductDeliveryAndPayment(
                freeDeliveryTotalPriceThreshold = product.freeDeliveryTotalPriceThreshold,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateZarinaItem(this),
            )
        }

        if (productState.totalLookState !is ProductSuggestionsState.None) {
            item(
                key = ProductListKey.TotalLook,
                contentType = ProductListContentType.Suggestions,
            ) {
                ProductSuggestions(
                    title = stringResource(R.string.product_suggestions_title_total_look),
                    state = productState.totalLookState,
                    onEvent = onProductSuggestionsEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateZarinaItem(this),
                )
            }
        }

        if (productState.similarProductsState !is ProductSuggestionsState.None) {
            item(
                key = ProductListKey.SimilarProducts,
                contentType = ProductListContentType.Suggestions,
            ) {
                ProductSuggestions(
                    title = stringResource(R.string.product_suggestions_title_similar_products),
                    state = productState.similarProductsState,
                    onEvent = onProductSuggestionsEvent,
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateZarinaItem(this),
                )
            }
        }
    }
}

@Composable
private fun BottomBar(
    isProductAvailable: Boolean,
    isProductInCart: Boolean,
    isProductInWishlist: Boolean,
    onAddProductToCartClicked: () -> Unit,
    onAddProductToWishlistClicked: () -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(contentPadding),
    ) {
        val buttonColors = when {
            !isProductAvailable -> ZarinaButtonDefaults.outlineColors()
            isProductInCart -> ZarinaButtonDefaults.outlineColors()
            else -> ZarinaButtonDefaults.primaryColors()
        }

        ZarinaButton(
            onClick = onAddProductToCartClicked,
            colors = buttonColors,
            modifier = Modifier.weight(1f),
        ) {
            val textResId = when {
                !isProductAvailable -> RCommon.string.res_notify_about_product_appearance
                isProductInCart -> RCommon.string.res_in_cart
                else -> RCommon.string.res_to_cart
            }

            Text(text = stringResource(textResId).uppercase())
        }

        Spacer(modifier = Modifier.width(8.dp))

        ZarinaLikeIconButton(
            isLiked = isProductInWishlist,
            onClick = onAddProductToWishlistClicked,
            iconSize = 20.dp,
        )
    }
}

@Parcelize
internal enum class ProductListKey : Parcelable {
    MediaPager,
    GeneralInfo,
    CheckAvailabilityInStoresButton,
    Description,
    DeliveryAndPayment,
    TotalLook,
    SimilarProducts,
}

private enum class ProductListContentType {
    MediaPager,
    GeneralInfo,
    CheckAvailabilityInStoresButton,
    Description,
    DeliveryAndPayment,
    Suggestions,
}

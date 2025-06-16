package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uicompose.pressBounce
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonDefaults
import ru.livetyping.zarina.core.uikit.button.ZarinaButtonSize
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductActionButtonState
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun FloatingButtonBlock(
    product: ProductDetailed,
    actionButtonState: ProductActionButtonState,
    onAddToCartClicked: (Product) -> Unit,
    onSubscribeClicked: (Product) -> Unit,
    onAddToWishlistClicked: (Product) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier) {
        val buttonSize = ZarinaButtonSize.Large

        ZarinaButton(
            onClick = {
                when (actionButtonState) {
                    is ProductActionButtonState.AddToCart, is ProductActionButtonState.InCart -> {
                        onAddToCartClicked(product)
                    }

                    ProductActionButtonState.NotifyWhenAvailable -> {
                        onSubscribeClicked(product)
                    }
                }
            },
            isLoading = actionButtonState.isLoading,
            size = buttonSize,
            modifier = Modifier.weight(1f),
        ) {
            AnimatedContent(
                targetState = actionButtonState,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec.using(sizeTransform = null)
                },
                contentKey = {
                    when (it) {
                        is ProductActionButtonState.AddToCart -> ActionButtonContentKey.AddToCart
                        is ProductActionButtonState.InCart -> ActionButtonContentKey.InCart
                        ProductActionButtonState.NotifyWhenAvailable -> ActionButtonContentKey.NotifyWhenAvailable
                    }
                },
            ) { state ->
                val textResId = when (state) {
                    is ProductActionButtonState.AddToCart -> RCommon.string.res_add_to_cart
                    is ProductActionButtonState.InCart -> RCommon.string.res_in_cart
                    ProductActionButtonState.NotifyWhenAvailable -> RCommon.string.res_notify_about_product_appearance
                }

                Text(text = stringResource(textResId).uppercase())
            }
        }

        Spacer(modifier = Modifier.width(4.dp))

        val interactionSource = remember { MutableInteractionSource() }

        ZarinaButton(
            onClick = { onAddToWishlistClicked(product) },
            size = buttonSize,
            interactionSource = interactionSource,
            contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
        ) {
            Crossfade(
                targetState = product.isInWishlist,
                modifier = Modifier.pressBounce(interactionSource),
            ) { inWishlist ->
                val iconResId = if (inWishlist) {
                    RCommon.drawable.ic_heart_24
                } else {
                    RCommon.drawable.ic_heart_outline_24
                }
                val contentDescResId = if (inWishlist) {
                    RCommon.string.res_remove_from_wishlist
                } else {
                    RCommon.string.res_add_to_wishlist
                }

                Icon(
                    imageVector = ImageVector.vectorResource(iconResId),
                    contentDescription = stringResource(contentDescResId),
                    tint = UiKitTheme2.colors.white,
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }
}

internal val FloatingButtonBlockHeight = ZarinaButtonDefaults.SizeLarge

private enum class ActionButtonContentKey { AddToCart, InCart, NotifyWhenAvailable }

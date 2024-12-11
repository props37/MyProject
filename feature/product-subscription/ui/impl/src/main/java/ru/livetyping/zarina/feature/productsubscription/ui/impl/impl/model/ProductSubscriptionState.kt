package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductOffer

@Stable
internal data class ProductSubscriptionState(
    val product: Product,
    val productOffer: ProductOffer,
    val nameTextFieldState: TextFieldState,
    val isNameInvalid: Boolean,
    val emailTextFieldState: TextFieldState,
    val isEmailInvalid: Boolean,
    val isSubscribeButtonLoading: Boolean,
)

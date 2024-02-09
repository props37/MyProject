package ru.zarina.zarina.ui.screen.productsubscription

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.zarina.zarina.domain.rework.common.Url
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.domain.rework.product.ProductOffer
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.product.ProductOfferParcelable
import ru.zarina.zarina.ui.model.product.ProductParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.productsubscription.ProductSubscriptionViewModel.SideEffect
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductSubscriptionViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductSubscriptionInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val product: StateFlow<Product> = savedStateHandle
        .getStateFlow<ProductParcelable?>(
            key = UnscopedDestinations.ProductSubscription.ARG_KEY_PRODUCT,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "product is null" }
            parcelable.toProduct()
        }

    val productOffer: StateFlow<ProductOffer> = savedStateHandle
        .getStateFlow<ProductOfferParcelable?>(
            key = UnscopedDestinations.ProductSubscription.ARG_KEY_OFFER,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            checkNotNull(parcelable) { "productOffer is null" }
            parcelable.toProductOffer()
        }

    val name: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_NAME,
        initialValue = "",
    )

    val email: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_EMAIL,
        initialValue = "",
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val result = ProductSubscriptionScreenResult.ScreenClosed
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onNameChanged(name: String) {
        savedStateHandle[KEY_NAME] = name
    }

    fun onEmailChanged(email: String) {
        savedStateHandle[KEY_EMAIL] = email
    }

    fun onUrlClicked(url: Url) {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.OpenUrl(url))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: ProductSubscriptionScreenResult) : SideEffect

        data class OpenUrl(val url: Url) : SideEffect
    }

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_EMAIL = "email"
    }
}

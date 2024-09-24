package ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import javax.inject.Inject

@HiltViewModel
class CheckoutSelectedPickupStoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val params = savedStateHandle.toRoute<CheckoutGraph.SelectedPickupStore>(
        typeMap = CheckoutGraph.SelectedPickupStore.typeMap(),
    )

    val store: StateFlow<Store> = ImmutableStateFlow(params.store.toStore())

    val availableProducts: StateFlow<List<CartProduct>> = ImmutableStateFlow(
        value = params.availableProducts.map { it.toCartProduct() },
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutSelectedPickupStoreScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onContinueClicked() {
        navigationThrottler.throttle {
            val cartType = params.cartType.toCartType()
            val checkoutParams = StorePickupCheckoutParams(
                cartType = cartType,
                deliveryMethodType = params.deliveryMethodType.toDeliveryMethodType(),
                city = params.city.toCity(),
                storeId = store.value.id,
            )
            val action = CheckoutSelectedPickupStoreScreenAction.ContinueClicked(
                cartType = cartType,
                step = params.step + 1,
                checkoutParams = checkoutParams,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutSelectedPickupStoreScreenAction) : SideEffect
    }
}

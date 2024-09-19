package ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.cart.CartProduct
import ru.livetyping.zarina.domain.cart.CartType
import ru.livetyping.zarina.domain.checkout.StorePickupCheckoutParams
import ru.livetyping.zarina.domain.geography.City
import ru.livetyping.zarina.domain.order.DeliveryMethodType
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.cart.CartProductParcelable
import ru.livetyping.zarina.presentation.model.cart.CartTypeParcelable
import ru.livetyping.zarina.presentation.model.geography.CityParcelable
import ru.livetyping.zarina.presentation.model.order.DeliveryMethodTypeParcelable
import ru.livetyping.zarina.presentation.model.store.StoreParcelable
import ru.livetyping.zarina.presentation.navigation.destination.graph.CheckoutGraph
import ru.livetyping.zarina.presentation.screen.checkout.selectedpickupstore.CheckoutSelectedPickupStoreViewModel.SideEffect
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class CheckoutSelectedPickupStoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val cartType: StateFlow<CartType> = savedStateHandle
        .getStateFlow<CartTypeParcelable?>(
            key = CheckoutGraph.SelectedPickupStore.ARG_KEY_CART_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "cartType is null" }
            it.toCartType()
        }

    private val step: StateFlow<Int> = savedStateHandle
        .getStateFlow<Int?>(
            key = CheckoutGraph.SelectedPickupStore.ARG_KEY_STEP,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "step is null" }
        }

    private val deliveryMethodType: StateFlow<DeliveryMethodType> = savedStateHandle
        .getStateFlow<DeliveryMethodTypeParcelable?>(
            key = CheckoutGraph.SelectedPickupStore.ARG_DELIVERY_METHOD_TYPE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "deliveryMethodType is null" }
            it.toDeliveryMethodType()
        }

    private val city: StateFlow<City> = savedStateHandle
        .getStateFlow<CityParcelable?>(
            key = CheckoutGraph.SelectedPickupStore.ARG_KEY_CITY,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "city is null" }
            it.toCity()
        }

    val store: StateFlow<Store> = savedStateHandle
        .getStateFlow<StoreParcelable?>(
            key = CheckoutGraph.SelectedPickupStore.ARG_KEY_STORE,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) {
            checkNotNull(it) { "store is null" }
            it.toStore()
        }

    val availableProducts: StateFlow<List<CartProduct>> = savedStateHandle
        .getStateFlow<Array<CartProductParcelable>?>(
            key = CheckoutGraph.SelectedPickupStore.ARG_KEY_AVAILABLE_PRODUCTS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { array ->
            checkNotNull(array) { "availableProducts is null" }
            array.map { it.toCartProduct() }
        }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = CheckoutSelectedPickupStoreScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onContinueClicked() {
        navigationThrottler.throttle {
            val cartType = cartType.value
            val checkoutParams = StorePickupCheckoutParams(
                cartType = cartType,
                deliveryMethodType = DeliveryMethodType.RETAIL,
                city = city.value,
                storeId = store.value.id,
            )
            val action = CheckoutSelectedPickupStoreScreenAction.ContinueClicked(
                cartType = cartType,
                step = step.value + 1,
                checkoutParams = checkoutParams,
            )
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CheckoutSelectedPickupStoreScreenAction) : SideEffect
    }
}

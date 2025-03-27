package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.StateFlow
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.checkout.PickupFromStoreCheckoutParams
import ru.livetyping.zarina.core.domain.model.store.Store
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import javax.inject.Inject

@HiltViewModel
internal class SelectedPickupStoreViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : ViewModel(), SideEffectSource<SelectedPickupStoreSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<SelectedPickupStoreNavEntry>(
        typeMap = SelectedPickupStoreNavEntry.typeMap(),
    )

    val store: StateFlow<Store> = ReadOnlyStateFlow(navEntry.store.toStore())

    private val availableProductsValue = navEntry.availableProducts
        .map { it.toCartProduct() }
        .toImmutableList()

    val availableProducts: StateFlow<ImmutableList<CartProduct>> =
        ReadOnlyStateFlow(availableProductsValue)

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = SelectedPickupStoreScreenAction.BackClicked
            emitSideEffect(SelectedPickupStoreSideEffect.Navigate(action))
        }
    }

    fun onContinueClicked() {
        navigationThrottler.throttle {
            val checkoutParams = PickupFromStoreCheckoutParams(
                cartType = navEntry.cartType.toCartType(),
                deliveryMethod = navEntry.deliveryMethod.toDeliveryMethod(),
                recipient = navEntry.recipient.toRecipient(),
                city = navEntry.city.toCity(),
                store = navEntry.store.toStore(),
            )
            val action = SelectedPickupStoreScreenAction.ContinueClicked(
                currentCheckoutStep = navEntry.checkoutStep,
                checkoutParams = checkoutParams,
            )
            emitSideEffect(SelectedPickupStoreSideEffect.Navigate(action))
        }
    }
}

package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.cart.Cart
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class PickupStoreSelectorStateBuilder {
    fun build(
        cartResult: Result<Cart>?,
        storesResult: Result<List<PickupStore>>?,
        cartLoadingState: FlowRequester.LoadingState,
        storesLoadingState: FlowRequester.LoadingState,
        city: City?,
    ): PickupStoreSelectorState {
        if (
            cartResult == null ||
            storesResult == null ||
            cartLoadingState.isLoading() ||
            storesLoadingState.isLoading()
        ) return PickupStoreSelectorState.Loading

        return if (cartResult.isSuccess && storesResult.isSuccess) {
            val cart = cartResult.getOrThrow()
            val stores = storesResult.getOrThrow()
            PickupStoreSelectorState.Success(
                stores = stores.toImmutableList(),
                cartItemCount = cart.products.size,
                city = city,
            )
        } else {
            val exception = cartResult.exceptionOrNull() ?: storesResult.exceptionOrNull()
            val errorState = exception?.let { ZarinaErrorScreenState.from(it) }
                ?: ZarinaErrorScreenState.GENERIC
            PickupStoreSelectorState.Error(errorState)
        }
    }
}

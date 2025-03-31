package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.checkout.PickupPoint
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class PickupPointListStateBuilder {
    fun build(
        pickupPointResult: Result<List<PickupPoint>>,
        pickupPointLoadingState: FlowRequester.LoadingState,
        filterQuery: String,
        appliedFilters: List<Filter>,
    ): PickupPointListState {
        return if (pickupPointLoadingState.isLoading()) {
            PickupPointListState.Loading
        } else {
            pickupPointResult.fold(
                onSuccess = { pickupPoints ->
                    val filteredPickupPoints =
                        filterPickupPoints(pickupPoints, filterQuery, appliedFilters)
                    PickupPointListState.Success(filteredPickupPoints.toImmutableList())
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    PickupPointListState.Error(errorState)
                },
            )
        }
    }

    private fun filterPickupPoints(
        pickupPoints: List<PickupPoint>,
        filterQuery: String,
        appliedFilters: List<Filter>,
    ): List<PickupPoint> {
        return pickupPoints.filter { pickupPoint ->
            var matchFilters = true
            appliedFilters.forEach { filter ->
                matchFilters = when (filter) {
                    Filter.PAYMENT_BY_CARD -> matchFilters && pickupPoint.isPaymentByCardAvailable
                    Filter.FITTING -> matchFilters && pickupPoint.isFittingAvailable
                }
            }
            if (!matchFilters) return@filter false

            pickupPoint.title.contains(filterQuery, ignoreCase = true)
                    || pickupPoint.address.contains(filterQuery, ignoreCase = true)
        }
    }
}

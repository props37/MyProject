package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.product.ProductAvailabilityInStore
import ru.livetyping.zarina.core.domain.model.product.exception.ProductNotAvailableException
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class StoreListStateBuilder {
    fun build(
        productAvailabilityListResult: Result<List<ProductAvailabilityInStore>>?,
        loadingState: FlowRequester.LoadingState,
    ): StoreListState {
        return if (loadingState.isLoading() || productAvailabilityListResult == null) {
            StoreListState.Loading
        } else {
            productAvailabilityListResult.fold(
                onSuccess = { availabilityList ->
                    if (availabilityList.isNotEmpty()) {
                        StoreListState.Success(availabilityList.toImmutableList())
                    } else {
                        StoreListState.Empty
                    }
                },
                onFailure = { t ->
                    when (t) {
                        is ProductNotAvailableException -> StoreListState.Empty
                        else -> {
                            val errorState = ZarinaErrorScreenState.from(t)
                            StoreListState.Error(errorState)
                        }
                    }
                },
            )
        }
    }
}

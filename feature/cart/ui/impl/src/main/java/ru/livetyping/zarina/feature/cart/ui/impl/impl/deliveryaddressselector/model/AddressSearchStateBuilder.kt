package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model

import kotlinx.collections.immutable.toImmutableList
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.model.common.exception.EmptySearchQueryException
import ru.livetyping.zarina.core.domain.model.geo.AddressPart
import ru.livetyping.zarina.core.domain.model.geo.exception.AddressNotFoundException
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState

internal class AddressSearchStateBuilder {
    fun build(
        searchResult: Result<List<AddressPart>>,
        loadingState: FlowRequester.LoadingState,
    ): AddressSearchState {
        return if (loadingState.isLoading()) {
            AddressSearchState.Loading
        } else {
            searchResult.fold(
                onSuccess = { addressList ->
                    val items = addressList
                        .map { AddressSearchItem(it) }
                        .toImmutableList()
                    AddressSearchState.Success(items)
                },
                onFailure = { t ->
                    when (t) {
                        is EmptySearchQueryException, is AddressNotFoundException -> {
                            AddressSearchState.Empty
                        }

                        else -> {
                            val errorState = ZarinaErrorScreenState.from(t)
                            AddressSearchState.Error(errorState)
                        }
                    }
                }
            )
        }
    }
}

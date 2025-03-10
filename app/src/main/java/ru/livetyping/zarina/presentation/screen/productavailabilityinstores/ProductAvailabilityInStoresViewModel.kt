package ru.livetyping.zarina.presentation.screen.productavailabilityinstores

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.product.ProductAvailabilityInStore
import ru.livetyping.zarina.domain.product.ProductOffer
import ru.livetyping.zarina.domain.product.exception.ProductNotAvailableException
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.productavailabilityinstores.ProductAvailabilityInStoresViewModel.SideEffect
import ru.livetyping.zarina.usecase.product.GetProductAvailabilityInStoresFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductAvailabilityInStoresViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductAvailabilityInStoresInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<UnscopedDestinations.ProductAvailabilityInStores>(
        typeMap = UnscopedDestinations.ProductAvailabilityInStores.typeMap(),
    )
    private val product = navEntry.product.toProductItem()

    private val selectedOffer: MutableStateFlow<ProductOffer?> = MutableStateFlow(
        value = product.offers.firstOrNull(),
    )

    val offers: StateFlow<ImmutableList<OfferItem>> = selectedOffer.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { selectedOffer ->
        val heightSet = product.offers.mapTo(mutableSetOf()) { it.height }
        val isHeightVisible = heightSet.size > 1
        product.offers
            .map { offer ->
                OfferItem(
                    offer = offer,
                    isHeightVisible = isHeightVisible,
                    isSelected = offer.barcode == selectedOffer?.barcode,
                )
            }
            .toImmutableList()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val availabilityRequester = FlowRequester(AvailabilityRequest) { request ->
        selectedOffer.flatMapLatest { selectedOffer ->
            if (selectedOffer != null) {
                markAsLoading(request)
                val params = GetProductAvailabilityInStoresFlowUseCase.Params(selectedOffer)
                interactor.getProductAvailabilityInStoresFlow(params)
            } else {
                flowOf(null)
            }
        }
    }

    val availabilityState: StateFlow<AvailabilityState> = combine(
        availabilityRequester.flow,
        availabilityRequester.loadingState,
    ) { result, loadingState ->
        if (loadingState.isLoading() || result == null) {
            AvailabilityState.Loading
        } else {
            result.fold(
                onSuccess = { availability ->
                    if (availability.isNotEmpty()) {
                        AvailabilityState.Success(availability.toImmutableList())
                    } else {
                        AvailabilityState.NotAvailable
                    }
                },
                onFailure = { t ->
                    when (t) {
                        is ProductNotAvailableException -> AvailabilityState.NotAvailable
                        else -> {
                            val errorState = ErrorState.from(t)
                            AvailabilityState.Error(errorState)
                        }
                    }
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = AvailabilityState.Loading,
    )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductAvailabilityInStoresScreenAction.BackClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onOfferClicked(offerItem: OfferItem) {
        selectedOffer.value = offerItem.offer
    }

    fun onErrorRefreshClicked() {
        availabilityRequester.request(AvailabilityRequest)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductAvailabilityInStoresScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @Immutable
    data class OfferItem(
        val offer: ProductOffer,
        val isHeightVisible: Boolean,
        val isSelected: Boolean,
    )

    @Stable
    sealed class AvailabilityState {
        @Immutable
        data class Success(
            val availability: ImmutableList<ProductAvailabilityInStore>,
        ) : AvailabilityState()

        data object NotAvailable : AvailabilityState()

        data object Loading : AvailabilityState()

        @Immutable
        data class Error(val errorState: ErrorState) : AvailabilityState()
    }

    private data object AvailabilityRequest : FlowRequester.Request
}

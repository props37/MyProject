package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.AvailabilityInStoresEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.AvailabilityInStoresState
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.Size
import javax.inject.Inject

@HiltViewModel
internal class AvailabilityInStoresViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getUserCityFlow: GetUserCityFlowUseCase,
) : ViewModel(), SideEffectSource<AvailabilityInStoresSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<AvailabilityInStoresNavEntry>(
        typeMap = AvailabilityInStoresNavEntry.typeMap(),
    )
    private val product = navEntry.product.toProductShort()

    private val selectedOffer = MutableStateFlow<ProductOffer?>(
        value = getInitiallySelectedOffer(product.offers)
    )

    private val sizes = selectedOffer.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
    ) { selectedOffer ->
        val heightSet = product.offers.mapTo(mutableSetOf()) { it.height }
        val isHeightVisible = heightSet.size > 1
        product.offers
            .map { offer ->
                Size(
                    offer = offer,
                    isHeightVisible = isHeightVisible,
                    isSelected = offer.barcode == selectedOffer?.barcode,
                )
            }
            .toImmutableList()
    }

    private val getCityParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
    private val cityFlow = getUserCityFlow(getCityParams).map { it.getOrNull() }

    val availabilityInStoresState: StateFlow<AvailabilityInStoresState> = combine(
        sizes,
        cityFlow,
    ) { sizes, city ->
        AvailabilityInStoresState(sizes, city)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = AvailabilityInStoresState(
            sizes = sizes.value,
            city = null,
        ),
    )

    fun onAvailabilityInStoresEvent(event: AvailabilityInStoresEvent) {
        when (event) {
            AvailabilityInStoresEvent.BackClicked -> onBackClicked()
            is AvailabilityInStoresEvent.SizeClicked -> onSizeClicked(event)
            AvailabilityInStoresEvent.ErrorRefreshClicked -> onErrorRefreshClicked()
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = AvailabilityInStoresScreenAction.BackClicked
            emitSideEffect(AvailabilityInStoresSideEffect.Navigate(action))
        }
    }

    private fun onSizeClicked(event: AvailabilityInStoresEvent.SizeClicked) {
        selectedOffer.value = event.size.offer
    }

    private fun onErrorRefreshClicked() {
        // TODO: [Top] Implement
//        availabilityRequester.request(AvailabilityRequest)
    }

    private fun getInitiallySelectedOffer(offers: List<ProductOffer>): ProductOffer? {
        return offers.firstOrNull { it.isAvailableInStores } ?: offers.firstOrNull()
    }
}

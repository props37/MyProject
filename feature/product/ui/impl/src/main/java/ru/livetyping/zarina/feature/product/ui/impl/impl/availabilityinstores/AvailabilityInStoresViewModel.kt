package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
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
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.SizeStateBuilder
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

    private val selectedOffer = MutableStateFlow(getInitiallySelectedOffer(product.offers))

    private val sizeStateBuilder = SizeStateBuilder()
    private val sizeState = selectedOffer.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
    ) { selectedOffer ->
        sizeStateBuilder.build(product.offers, selectedOffer)
    }

    private val getCityParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
    private val cityFlow = getUserCityFlow(getCityParams).map { it.getOrNull() }

    val availabilityInStoresState: StateFlow<AvailabilityInStoresState> = combine(
        sizeState,
        cityFlow,
    ) { sizeState, city ->
        AvailabilityInStoresState(sizeState, city)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = AvailabilityInStoresState(
            sizeState = sizeState.value,
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

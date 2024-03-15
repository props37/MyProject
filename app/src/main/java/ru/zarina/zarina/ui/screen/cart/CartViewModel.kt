package ru.zarina.zarina.ui.screen.cart

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.cart.DeliveryType
import ru.zarina.zarina.domain.geography.City
import ru.zarina.zarina.ui.common.base.ErrorState
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.from
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.common.util.ScreenResultHandler
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.cart.CartViewModel.SideEffect
import ru.zarina.zarina.usecase.cart.GetCartFlowUseCase
import ru.zarina.zarina.usecase.user.SetUserCityUseCase
import ru.zarina.zarina.util.base.usecase.invoke
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import ru.zarina.zarina.domain.cart.Cart as DomainCart

@HiltViewModel(assistedFactory = CartViewModel.Factory::class)
class CartViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    savedStateHandle: SavedStateHandle,
    private val interactor: CartInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

    val isCartEmpty: StateFlow<Boolean> = interactor.getCartProductCountFlow()
        .map { result ->
            val count = result.getOrDefault(0)
            count <= 0
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = true,
        )

    val city: StateFlow<City?> = interactor.getUserCityFlow()
        .map { result ->
            result.getOrNull() ?: City.DEFAULT
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    val isClearCartButtonVisible: StateFlow<Boolean> = isCartEmpty.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { isCartEmpty -> !isCartEmpty }

    val deliveryTypes: StateFlow<ImmutableList<DeliveryType>> =
        MutableStateFlow(DeliveryType.entries.toImmutableList()).asStateFlow()

    private val _currentDeliveryType = MutableStateFlow(DeliveryType.DELIVERY)
    val currentDeliveryType: StateFlow<DeliveryType> = _currentDeliveryType.asStateFlow()

    private val cartFetchRequests = Channel<Unit>(Channel.CONFLATED)

    val deliveryTypeToCartState: StateFlow<ImmutableMap<DeliveryType, StateFlow<CartState>>> =
        combine(
            cartFetchRequests.receiveAsFlow(),
            deliveryTypes,
        ) { _, deliveryTypes ->
            deliveryTypes
                .associateWith { type ->
                    val params = GetCartFlowUseCase.Params(type)
                    interactor.getCartFlow(params)
                        .map { result ->
                            result.fold(
                                onSuccess = {
                                    if (it.products.isNotEmpty()) {
                                        CartState.Cart(it)
                                    } else {
                                        CartState.EmptyCart
                                    }
                                },
                                onFailure = { throwable ->
                                    val errorState = ErrorState.from(throwable)
                                    CartState.Error(errorState)
                                },
                            )
                        }
                        .stateIn(
                            scope = viewModelScope,
                            started = SharingStarted.WhileUiSubscribed,
                            initialValue = CartState.InitialLoading,
                        )
                }
                .toImmutableMap()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = deliveryTypes.value
                .associateWith { MutableStateFlow(CartState.InitialLoading) }
                .toImmutableMap(),
        )

    init {
        cartFetchRequests.trySend(Unit)

        handleCitySelectorResult()
    }

    fun onScreenOpened() {
        cartFetchRequests.trySend(Unit)
    }

    fun onClearCartClicked() {
        viewModelScope.launch {
            interactor.clearCart()
                .onSuccess {
                    // TODO: [High] Test
                    cartFetchRequests.trySend(Unit)
                }
                .onFailure {
                    val message = Text.Resource(R.string.cart_clearing_error_toast)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    fun onCityClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.CityClicked(city.value)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onDeliveryTypeChanged(type: DeliveryType) {
        _currentDeliveryType.value = type
    }

    fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = CartScreenAction.GoToCatalogClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun handleCitySelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.CitySelector.Result>(
                key = UnscopedDestinations.CitySelector.RESULT_KEY,
            ) { result ->
                val newCity = result.city.toCity()
                val currentCity = city.value
                if (newCity.kladrId != currentCity?.kladrId) {
                    val params = SetUserCityUseCase.Params(newCity)
                    interactor.setUserCity(params)
                        .onFailure {
                            val message = Text.Resource(R.string.city_changing_error_toast)
                            emitSideEffect(SideEffect.ShowToast(message))
                        }
                }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CartScreenAction) : SideEffect
        data class ShowToast(val message: Text) : SideEffect
    }

    @Stable
    sealed class CartState {
        data object InitialLoading : CartState()

        data class Cart(val cart: DomainCart) : CartState()

        data object EmptyCart : CartState()

        data class Error(val state: ErrorState) : CartState()
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): CartViewModel
    }
}


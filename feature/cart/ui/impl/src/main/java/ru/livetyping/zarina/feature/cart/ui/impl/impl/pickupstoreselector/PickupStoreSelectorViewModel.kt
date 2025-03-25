package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.ReadOnlyStateFlow
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.checkout.PickupStore
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.checkout.GetPickupStoresFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.user.GetUserCityFlowUseCase
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.toast.ZarinaToastMessage
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarEvent
import ru.livetyping.zarina.feature.cart.ui.impl.impl.component.topbar.CheckoutTopBarState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.model.PickupStoreSelectorState
import ru.livetyping.zarina.feature.cart.ui.impl.impl.pickupstoreselector.model.PickupStoreSelectorStateBuilder
import ru.livetyping.zarina.feature.cart.ui.impl.impl.util.checkoutStepCount
import javax.inject.Inject
import ru.livetyping.zarina.core.resource.R as RCommon

@HiltViewModel
internal class PickupStoreSelectorViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: PickupStoreSelectorDependencies,
) : ViewModel(), SideEffectSource<PickupStoreSelectorSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<PickupStoreSelectorNavEntry>(
        typeMap = PickupStoreSelectorNavEntry.typeMap(),
    )
    private val cartType = navEntry.cartType.toCartType()
    private val deliveryMethodType = navEntry.deliveryMethodType.toDeliveryMethodType()
    private val checkoutStep = navEntry.checkoutStep

    val topBarState: StateFlow<CheckoutTopBarState> = ReadOnlyStateFlow(
        CheckoutTopBarState(
            checkoutStep = checkoutStep,
            checkoutStepCount = cartType.checkoutStepCount,
            isBackButtonVisible = true,
        )
    )

    private val getCityUseCaseParams = GetUserCityFlowUseCase.Params(CachePolicy.LocalOnly)
    private val cityFlow = deps.getUserCityFlow(getCityUseCaseParams)
        .map { it.getOrDefault(City.getDefault()) }

    private val cartRequester = FlowRequester(CartRequest) {
        val params = GetCartFlowUseCase.Params(cartType)
        deps.getCartFlow(params)
    }
    private val cartResultFlow = cartRequester.flow.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    private val storeRequester = FlowRequester(StoreRequester) {
        val params = GetPickupStoresFlowUseCase.Params(deliveryMethodType)
        deps.getPickupStoresFlow(params)
    }
    private val storeResultFlow = storeRequester.flow.shareIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        replay = 1,
    )

    private val pickupStoreSelectorStateBuilder = PickupStoreSelectorStateBuilder()
    val pickupStoreSelectorState: StateFlow<PickupStoreSelectorState> = combine(
        cartResultFlow,
        storeResultFlow,
        cartRequester.loadingState,
        storeRequester.loadingState,
        cityFlow,
    ) { cartResult, storeResult, cartLoadingState, storeLoadingState, city ->
        pickupStoreSelectorStateBuilder.build(
            cartResult = cartResult,
            storesResult = storeResult,
            cartLoadingState = cartLoadingState,
            storesLoadingState = storeLoadingState,
            city = city,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = PickupStoreSelectorState.Loading,
    )

    fun onTopBarEvent(event: CheckoutTopBarEvent) {
        when (event) {
            CheckoutTopBarEvent.BackClicked -> onBackClicked()
            CheckoutTopBarEvent.CloseClicked -> onCloseClicked()
        }
    }

    fun onStoreClicked(store: PickupStore) {
        navigationThrottler.throttle {
            viewModelScope.launch {
                val cart = cartResultFlow.firstOrNull()?.getOrNull()
                if (cart == null) {
                    val text = Text.Resource(RCommon.string.res_something_went_wrong)
                    showZarinaErrorToast(text)
                    return@launch
                }
                val availableProducts = cart.products.filter { it.offerId in store.availableItemIds }
                if (availableProducts.isEmpty()) {
                    val text = Text.Resource(R.string.cart_there_are_no_products_in_selected_store)
                    showZarinaErrorToast(text)
                    return@launch
                }

                val city = cityFlow.firstOrNull()
                if (city == null) {
                    val text = Text.Resource(RCommon.string.res_something_went_wrong)
                    showZarinaErrorToast(text)
                    return@launch
                }

                val action = PickupStoreSelectorScreenAction.StoreSelected(
                    cartType = cartType,
                    currentCheckoutStep = checkoutStep,
                    recipient = navEntry.recipient.toRecipient(),
                    deliveryMethodType = deliveryMethodType,
                    city = city,
                    store = store.store,
                    availableProducts = availableProducts,
                )
                emitSideEffect(PickupStoreSelectorSideEffect.Navigate(action))
            }
        }
    }

    fun onStoresErrorRefreshClicked() {
        viewModelScope.launch {
            if (cartResultFlow.firstOrNull()?.isSuccess != true) {
                cartRequester.request(CartRequest)
            }
            if (storeResultFlow.firstOrNull()?.isSuccess != true) {
                storeRequester.request(StoreRequester)
            }
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = PickupStoreSelectorScreenAction.BackClicked
            emitSideEffect(PickupStoreSelectorSideEffect.Navigate(action))
        }
    }

    private fun onCloseClicked() {
        navigationThrottler.throttle {
            val action = PickupStoreSelectorScreenAction.CloseClicked
            emitSideEffect(PickupStoreSelectorSideEffect.Navigate(action))
        }
    }

    private fun showZarinaErrorToast(text: Text) {
        val message = ZarinaToastMessage.error(text)
        emitSideEffect(PickupStoreSelectorSideEffect.ShowZarinaToast(message))
    }

    private data object CartRequest : FlowRequest

    private data object StoreRequester : FlowRequest
}

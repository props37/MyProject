package ru.zarina.zarina.ui.screens.pickup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Offer
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Stock
import ru.zarina.zarina.domain.exception.NotFoundException
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.utils.coroutine.mapState
import ru.zarina.zarina.utils.isNetworkException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PickupViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: PickupInteractor,
) : ViewModel() {

    private val operationTracker = OperationTracker()

    private val productId = savedStateHandle.getStateFlow(
        key = Pickup.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).mapState(viewModelScope) { Product.Id(it) }

    private val _city = MutableStateFlow<City?>(null)
    val city = _city.asStateFlow()

    private val _product = MutableStateFlow<Result<Product?>?>(null)
    val product = _product
        .mapState(viewModelScope) { it?.getOrNull() }

    private val offersReloadTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
        .apply { tryEmit(Unit) }
    private val _offers = MutableStateFlow<Result<List<Offer>>?>(null)
    val offers = _offers
        .mapState(viewModelScope) {
            it?.getOrNull()?.toPersistentList() ?: persistentListOf()
        }

    val selectedOffer = savedStateHandle.getStateFlow<Offer?>(KEY_SELECTED_OFFER, null)

    private val stockReloadTrigger = MutableSharedFlow<Unit>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
        .apply { tryEmit(Unit) }
    private val _stocks = MutableStateFlow<Result<List<Stock>>?>(null)
    val stocks = _stocks
        .map { it?.getOrNull()?.toPersistentList() }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val isStocksLoaderVisible = operationTracker
        .isOperationOngoing(
            Operation.LOADING_CITY,
            Operation.LOADING_PRODUCT,
            Operation.LOADING_SIZES,
            Operation.LOADING_STOCKS,
        )
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    val errorType = combine(
        _product,
        _offers,
        _stocks,
    ) { product, offers, stocks ->

        val exception = listOf(product, offers, stocks)
            .firstNotNullOfOrNull { it?.exceptionOrNull() }

        when {
            exception == null -> null
            exception.isNetworkException() -> ErrorType.NETWORK
            else -> ErrorType.GENERIC
        }
    }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    init {
        loadUserCity()
        setupStockLoading()
        setupSizeUpdates()
        setupProductLoading()
    }

    fun onOfferClick(offer: Offer) {
        savedStateHandle[KEY_SELECTED_OFFER] = offer
    }

    fun onStockPickupClick(stock: Stock) {
        // TODO
    }

    fun onRefreshClick() {
        when {
            _product.value?.isFailure == true -> loadProduct(productId.value)
            _offers.value?.isFailure == true -> offersReloadTrigger.tryEmit(Unit)
            _stocks.value?.isFailure == true -> stockReloadTrigger.tryEmit(Unit)
        }
    }

    fun onCityClick(city: City) {
        _city.value = city
    }

    private fun loadProduct(id: Product.Id) {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_PRODUCT) {
                _product.value = interactor.getProduct(id)
            }
        }
    }

    private fun loadUserCity() {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_CITY) {
                _city.value = interactor.getCity().getOrNull()
            }
        }
    }

    private fun setupStockLoading() {
        combine(selectedOffer, _city, stockReloadTrigger) { offer, city, _ ->
            if (offer == null || city == null) return@combine
            operationTracker.track(Operation.LOADING_STOCKS) {
                _stocks.value = interactor.getStocks(offer, city)
                    .recoverCatching { throwable ->
                        when (throwable) {
                            is NotFoundException -> persistentListOf()
                            else -> throw throwable
                        }
                    }
            }
        }
            .launchIn(viewModelScope)
    }

    private fun setupSizeUpdates() {
        combine(_product, _city, offersReloadTrigger) { productResult, city, _ ->
            val product = productResult?.getOrNull()
            if (product == null || city == null) return@combine
            operationTracker.track(Operation.LOADING_SIZES) {
                _offers.value = interactor.getOffers(product, city)
            }
        }
            .launchIn(viewModelScope)

        _offers
            .onEach { offersResult ->
                val offers = offersResult?.getOrNull() ?: return@onEach
                if (selectedOffer.value !in offers) savedStateHandle[KEY_SELECTED_OFFER] = null
            }
            .launchIn(viewModelScope)
    }

    private fun setupProductLoading() {
        productId
            .mapLatest { id -> loadProduct(id) }
            .launchIn(viewModelScope)
    }

    enum class ErrorType { NETWORK, GENERIC }

    enum class Operation : OperationKey {
        LOADING_CITY, LOADING_PRODUCT, LOADING_SIZES, LOADING_STOCKS
    }

    companion object {
        private const val KEY_SELECTED_OFFER = "selected_offer"
    }

}

package ru.zarina.zarina.ui.screens.pickup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.utils.coroutine.mapState
import ru.zarina.zarina.utils.isNetworkException
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class PickupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: PickupInteractor,
) : ViewModel() {

    private val _errorType = MutableStateFlow<ErrorType?>(null)
    val errorType = _errorType.asStateFlow()

    private val productId = savedStateHandle.getStateFlow(
        key = Pickup.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).mapState(viewModelScope) { Product.Id(it) }

    private val _city = MutableStateFlow<City?>(null)
    val city = _city.asStateFlow()

    private val _product = MutableStateFlow<Product?>(null)
    val product = _product.asStateFlow()

    private val _offers = MutableStateFlow<List<Offer>>(emptyList())
    val offers = _offers
        .map { offers ->
            offers.toPersistentList()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    private val _selectedOffer = MutableStateFlow<Offer?>(null)
    val selectedOffer = _selectedOffer.asStateFlow()

    init {
        loadUserCity()
        setupSizeUpdates()
        setupProductLoading()
    }

    fun onOfferClick(offer: Offer) {
        _selectedOffer.value = offer
    }

    fun onRefreshClick() {
        loadProduct(productId.value)
    }

    fun onCityClick(city: City) {
        _city.value = city
    }

    private fun loadProduct(id: Product.Id) {
        viewModelScope.launch {
            // TODO loader
            interactor.getProduct(id)
                .onSuccess {
                    _product.value = it
                    _errorType.value = null
                }
                .onFailure { throwable ->
                    _errorType.value = when {
                        throwable is CancellationException -> return@onFailure
                        throwable.isNetworkException() -> ErrorType.NETWORK
                        else -> ErrorType.GENERIC
                    }
                }
        }
    }

    private fun loadUserCity() {
        // TODO loader
        // TODO error
        viewModelScope.launch {
            _city.value = interactor.getCity().getOrNull()
        }
    }

    private fun setupSizeUpdates() {
        combine(_product, _city) { product, city ->
            if (product == null || city == null) return@combine
            // TODO loader
            // TODO error
            interactor.getOffers(product, city)
                .onSuccess { _offers.value = it }
        }
            .launchIn(viewModelScope)

        _offers
            .onEach { offers ->
                val selectedSizeName = _selectedOffer.value?.size?.name
                val sameNameOffer = offers.find { it.size.name == selectedSizeName }
                val availableOffer = offers.find { it.isAvailable }
                val firstOffer = offers.firstOrNull()
                _selectedOffer.value = sameNameOffer ?: availableOffer ?: firstOffer
            }
            .launchIn(viewModelScope)
    }

    private fun setupProductLoading() {
        productId
            .mapLatest { id -> loadProduct(id) }
            .launchIn(viewModelScope)
    }

    enum class ErrorType { NETWORK, GENERIC }

}

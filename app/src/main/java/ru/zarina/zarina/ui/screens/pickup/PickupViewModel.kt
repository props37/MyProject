package ru.zarina.zarina.ui.screens.pickup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.City
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
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

    val sizes = product
        .map { product ->
            product
                ?.offers
                ?.map { it.size }
                .orEmpty()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    private val _selectedSize = MutableStateFlow<Size?>(null)
    val selectedSize = _selectedSize.asStateFlow()

    init {
        loadUserCity()
        setupSizeUpdates()
        setupProductLoading()
    }

    fun onSizeClick(size: Size) {
        _selectedSize.value = size
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
        product
            .onEach { product ->
                _selectedSize.value = (product?.offers
                    ?.find { it.isAvailable }
                    ?: product?.offers?.firstOrNull())
                    ?.size
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

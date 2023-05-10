package ru.zarina.zarina.ui.screens.pickup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.ui.navigation.destinations.Pickup
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class PickupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: PickupInteractor,
) : ViewModel() {

    private val productId = savedStateHandle.getStateFlow(
        key = Pickup.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).mapState(viewModelScope) { Product.Id(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val product = productId
        .mapLatest {
            // TODO loader
            // TODO errors
            interactor.getProduct(it).getOrNull()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

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
        setupSizeUpdates()
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

}

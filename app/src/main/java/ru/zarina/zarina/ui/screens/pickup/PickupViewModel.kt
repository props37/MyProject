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
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Size
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class PickupViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: PickupInteractor,
) : ViewModel(),
    ISideEffectSource<PickupViewModel.SideEffect> by SideEffectQueue() {

    private val productId = savedStateHandle
        .getStateFlow(
            key = Destinations.PICKUP.ARGUMENT_PRODUCT_ID,
            initialValue = ""
        )
        .mapState(viewModelScope) { Product.Id(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val product = productId
        .mapLatest {
            // TODO loader
            // TODO errors
            interactor.getProduct(it).getOrNull()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val _selectedSize = MutableStateFlow<Size?>(null)
    val selectedSize = _selectedSize.asStateFlow()

    init {
        setupSizeUpdates()
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    private fun setupSizeUpdates() {
        product
            .onEach { product ->
                _selectedSize.value = product?.offers?.firstOrNull { it.isAvailable }?.size
            }
            .launchIn(viewModelScope)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
    }

}

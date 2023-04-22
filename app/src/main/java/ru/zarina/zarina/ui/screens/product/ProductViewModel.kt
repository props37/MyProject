package ru.zarina.zarina.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
) : ViewModel(),
    ISideEffectSource<ProductViewModel.SideEffect> by SideEffectQueue() {

    private val productId = savedStateHandle.get<String>(Destinations.PRODUCT.ARGUMENT_PRODUCT_ID)
    val product = MutableStateFlow<Product?>(null)

    init {
        getProduct()
    }

    private fun getProduct() {
        // TODO go back when product id is null
        if (productId == null) return
        viewModelScope.launch {
            interactor.getProduct(productId)
                .onSuccess { product.value = it }
                .onFailure { /* TODO show error */ }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}

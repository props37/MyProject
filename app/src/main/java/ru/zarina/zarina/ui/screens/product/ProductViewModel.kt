package ru.zarina.zarina.ui.screens.product

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.datasource.cache.Cache
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.navigation.destinations.Destinations
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
    cache: Cache,
) : ViewModel(),
    ISideEffectSource<ProductViewModel.SideEffect> by SideEffectQueue() {

    val cache = MutableStateFlow(cache).asStateFlow()
    private val productId = savedStateHandle.getStateFlow(
        key = Destinations.PRODUCT.ARGUMENT_PRODUCT_ID,
        initialValue = ""
    ).map { Product.Id(it) }

    @OptIn(ExperimentalCoroutinesApi::class)
    val product = productId
        .mapLatest { id ->
            // TODO show loading error
            interactor.getProduct(id).getOrNull()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val completeLookProducts = product
        .mapLatest { product ->
            // TODO show loading error
            if (product?.isLookPart == true)
                interactor.getCompleteLook(product).getOrDefault(emptyList())
            else
                emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val similarProducts = product
        .mapLatest { product ->
            // TODO show loading error
            if (product != null)
                interactor.getRecommendations(product).getOrDefault(emptyList())
            else
                emptyList()
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    @OptIn(ExperimentalCoroutinesApi::class)
    val deliveryAvailability = product
        .mapLatest { product ->
            // TODO show loader
            // TODO show loading error
            if (product != null)
                interactor.getDeliveryAvailability(product).getOrNull()
            else
                null
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun onVariantClick(variant: Product.Variant) {
        savedStateHandle[Destinations.PRODUCT.ARGUMENT_PRODUCT_ID] = variant.id
    }

    fun onShareClick() {
        val url = product.value?.url
        if (url != null) sideEffect(SideEffect.ShareText(url.value))
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product))
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShareText(val text: String) : SideEffect
        data class ShowProduct(val product: Product) : SideEffect
    }

}

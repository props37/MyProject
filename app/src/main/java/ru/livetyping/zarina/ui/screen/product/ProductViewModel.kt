package ru.livetyping.zarina.ui.screen.product

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductDetails
import ru.livetyping.zarina.ui.common.error.ErrorState
import ru.livetyping.zarina.ui.common.error.from
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.ui.screen.product.ProductViewModel.SideEffect
import ru.livetyping.zarina.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val productId: StateFlow<Product.Id> = savedStateHandle
        .getStateFlow<String?>(
            key = UnscopedDestinations.Product.ARG_KEY_PRODUCT_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "productId is null" }
            Product.Id(value)
        }

    private val productResult: StateFlow<Result<ProductDetails>?> =
        interactor.getProductFlow(GetProductFlowUseCase.Params(productId.value))
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val productState: StateFlow<ProductState> = productResult
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { result ->
            result?.fold(
                onSuccess = { product ->
                    ProductState.Success(product)
                },
                onFailure = {
                    val state = ErrorState.from(it)
                    ProductState.Error(state)
                },
            ) ?: ProductState.Loading
        }

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductScreenAction.ScreenClosed
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: ProductScreenAction) : SideEffect
    }

    @Stable
    sealed class ProductState {
        @Immutable
        data class Success(val product: ProductDetails) : ProductState()

        data object Loading : ProductState()

        @Immutable
        data class Error(val state: ErrorState) : ProductState()
    }
}

package ru.livetyping.zarina.feature.product.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.usecase.product.GetProductFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.feature.product.ui.api.ProductNavEntry
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.ProductState
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.model.TopBarState
import javax.inject.Inject

@HiltViewModel
internal class ProductViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val deps: ProductDeps,
) : ViewModel(), SideEffectSource<ProductSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<ProductNavEntry>()
    private val initialProductId = Product.Id(navEntry.productId)

    private val productId = MutableStateFlow(initialProductId)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val productRequester = FlowRequester(ProductRequest) { request ->
        productId.flatMapLatest { productId ->
            markAsLoading(request)
            val params = GetProductFlowUseCase.Params(productId)
            deps.getProductFlow(params)
        }
    }

    private val productResult = productRequester.flow
        .conflate()
        .shareIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            replay = 1,
        )

    val topBarState: StateFlow<TopBarState> = productResult
        .map { result ->
            val productName = result.getOrNull()?.name
            TopBarState(productName)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileAndroidUiSubscribed,
            initialValue = TopBarState(productName = null),
        )

    val productState: StateFlow<ProductState> = combine(
        productResult,
        productRequester.loadingState,
    ) { productResult, productLoadingState ->
        if (productLoadingState.isLoading()) {
            ProductState.Loading
        } else {
            productResult.fold(
                onSuccess = { product ->
                    ProductState.Success(product)
                },
                onFailure = {
                    val errorState = ZarinaErrorScreenState.from(it)
                    ProductState.Error(errorState)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = ProductState.Loading,
    )

    fun onTopBarEvent(event: TopBarEvent) {
        when (event) {
            TopBarEvent.BackClicked -> onBackClicked()
            TopBarEvent.ShareClicked -> shareProduct()
        }
    }

    fun onProductEvent(event: ProductEvent) {
        when (event) {
            is ProductEvent.ProductColorClicked -> {
                if (event.color.productId != productId.value) {
                    productId.value = event.color.productId
                }
            }

            // TODO: [Top] Implement
            is ProductEvent.AddToCartClicked -> TODO()
            is ProductEvent.AddToWishlistClicked -> TODO()
            ProductEvent.ErrorRefreshClicked -> productRequester.request(ProductRequest)
            ProductEvent.BonusAccrualForPurchaseClicked -> Unit // Handled completely on UI
        }
    }

    private fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductScreenAction.BackClicked
            emitSideEffect(ProductSideEffect.Navigate(action))
        }
    }

    private fun shareProduct() {
        viewModelScope.launch {
            val productShareUrl = productResult.firstOrNull()?.getOrNull()?.shareUrl
            if (productShareUrl != null) {
                emitSideEffect(ProductSideEffect.Share(productShareUrl.value))
            }
        }
    }

    private data object ProductRequest : FlowRequest
}

package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.onEachLatest
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker

internal class ProductComponent(
    private val getProductUseCase: GetProductUseCase,
    private val coroutineScope: CoroutineScope,
) {
    private val operationTracker = OperationTracker()

    private var productFetchingJob: Job? = null
    private var fetchProductJob: Job? = null

    private val productId = MutableStateFlow<Product.Id?>(null)

    private val _productResult = MutableStateFlow<Result<ProductDetailed>?>(null)
    val productResult: StateFlow<Result<ProductDetailed>?> = _productResult.asStateFlow()

    val isProductLoading: Flow<Boolean> = operationTracker.isOperationOngoing(ProductRequest)

    init {
        initProductFetching()
    }

    fun setProductId(id: Product.Id) {
        productId.value = id
    }

    suspend fun awaitProduct(): Product? {
        val successResult = productResult.firstOrNull { it?.isSuccess == true }
        return successResult?.getOrNull()
    }

    suspend fun fetchProduct() {
        if (fetchProductJob?.isActive == true) return
        val productId = requireProductId()
        fetchProductImpl(productId)
    }

    private fun initProductFetching() {
        _productResult.subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .onEach { isActive ->
                if (isActive) startProductFetching() else stopProductFetching()
            }
            .launchIn(coroutineScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startProductFetching() {
        productFetchingJob?.cancel()
        productFetchingJob = productId
            .filterNotNull()
            .distinctUntilChanged()
            .onEachLatest { id ->
                if (shouldFetchProduct(id)) fetchProductImpl(id)
            }
            .launchIn(coroutineScope)
    }

    private fun stopProductFetching() {
        productFetchingJob?.cancel()
    }

    private suspend fun fetchProductImpl(id: Product.Id) {
        if (fetchProductJob?.isActive == true) return
        coroutineScope {
            fetchProductJob = launch {
                operationTracker.track(ProductRequest) {
                    val params = GetProductUseCase.Params(id)
                    _productResult.value = getProductUseCase(params)
                }
            }
        }
    }

    private fun shouldFetchProduct(id: Product.Id): Boolean {
        val currentProductResult = productResult.value
        val currentProduct = currentProductResult?.getOrNull()
        return currentProduct?.id != id
    }

    private fun requireProductId(): Product.Id {
        return checkNotNull(productId.value) {
            "productId.value is null. Did you forgot to call setProductId?"
        }
    }

    private data object ProductRequest : OperationKey
}

package ru.livetyping.zarina.feature.product.ui.impl.impl.product.component

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.coroutinesutil.onEachLatest
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.product.Product
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductTotalLookUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetSimilarProductsUseCase
import ru.livetyping.zarina.core.domain.usecase.wishlist.GetWishlistProductIdsFlowUseCase
import ru.livetyping.zarina.core.uicommon.operation.OperationKey
import ru.livetyping.zarina.core.uicommon.operation.OperationTracker

internal class ProductComponent(
    private val getProductUseCase: GetProductUseCase,
    private val getProductTotalLookUseCase: GetProductTotalLookUseCase,
    private val getSimilarProductsUseCase: GetSimilarProductsUseCase,
    getWishlistProductIdsFlowUseCase: GetWishlistProductIdsFlowUseCase,
    getCartProductIdsFlowUseCase: GetCartProductIdsFlowUseCase,
    private val coroutineScope: CoroutineScope,
) {
    private val operationTracker = OperationTracker()

    private var productFetchingJob: Job? = null
    private var fetchProductJob: Job? = null

    private var totalLookProductsFetchingJob: Job? = null
    private var fetchTotalLookProductsJob: Job? = null

    private var similarProductsFetchingJob: Job? = null
    private var fetchSimilarProductsJob: Job? = null

    private val productId = MutableStateFlow<Product.Id?>(null)

    private val getWishlistProductIdsUseCaseParams =
        GetWishlistProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val getCartProductIdsUseCaseParams =
        GetCartProductIdsFlowUseCase.Params(CachePolicy.LocalFirstThenRemote())

    private val wishlistProductIds =
        getWishlistProductIdsFlowUseCase(getWishlistProductIdsUseCaseParams)
            .map { result ->
                result.getOrDefault(emptySet())
            }
            .shareIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )

    private val cartProductIds =
        getCartProductIdsFlowUseCase(getCartProductIdsUseCaseParams)
            .map { result ->
                result.getOrDefault(emptySet())
            }
            .shareIn(
                scope = coroutineScope,
                started = SharingStarted.WhileSubscribed(),
                replay = 1,
            )

    private val _productResult = MutableStateFlow<Result<ProductDetailed>?>(null)
    val productResult: StateFlow<Result<ProductDetailed>?> = _productResult
        .updateProductInternalState()
        .onEach { clearSelectedProductSizeAndHeight() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val isProductLoading: Flow<Boolean> = operationTracker.isOperationOngoing(ProductRequest)

    private val _selectedProductSize = MutableStateFlow<String?>(null)
    val selectedProductSize: StateFlow<String?> = _selectedProductSize.asStateFlow()

    private val productSizeToHeights = productResult
        .map { productResult ->
            val product = productResult?.getOrNull()
            product?.offers?.toSizeToHeightsMap() ?: emptyMap()
        }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyMap(),
        )

    val productSizes: StateFlow<List<String>> = productSizeToHeights
        .map { sizeToHeights -> sizeToHeights.keys.toList() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = emptyList(),
        )

    val shouldProductHeightBeSelected: StateFlow<Boolean> = combine(
        productSizeToHeights,
        selectedProductSize,
    ) { sizeToHeights, selectedSize ->
        if (selectedSize != null) {
            val selectedSizeHeights = sizeToHeights[selectedSize]
            selectedSizeHeights != null && selectedSizeHeights.size > 1
        } else {
            // Does any size have multiple heights
            sizeToHeights.any { (_, heights) -> heights.size > 1 }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false,
    )

    val productHeights: StateFlow<List<String>> = combine(
        productSizeToHeights,
        selectedProductSize,
    ) { sizeToHeights, selectedSize ->
        if (selectedSize != null) {
            val heights = sizeToHeights[selectedSize]
            if (heights != null && heights.size > 1) {
                heights
            } else {
                emptyList()
            }
        } else {
            emptyList()
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = emptyList(),
    )

    private val _selectedProductHeight = MutableStateFlow<String?>(null)
    val selectedProductHeight: StateFlow<String?> = _selectedProductHeight.asStateFlow()

    private val _totalLookProductsResult = MutableStateFlow<Result<List<ProductShort>>?>(null)
    val totalLookProductsResult: StateFlow<Result<List<ProductShort>>?> = _totalLookProductsResult
        .updateProductListInternalState()
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val areTotalLookProductsLoading: Flow<Boolean> =
        operationTracker.isOperationOngoing(TotalLookProductsRequest)

    private val _similarProductsResult = MutableStateFlow<Result<List<ProductShort>>?>(null)
    val similarProductsResult: StateFlow<Result<List<ProductShort>>?> = _similarProductsResult
        .updateProductListInternalState()
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val areSimilarProductsLoading: Flow<Boolean> =
        operationTracker.isOperationOngoing(SimilarProductsRequest)

    init {
        initProductFetching()
        initTotalLookProductsFetching()
        initSimilarProductsFetching()
    }

    fun setProductId(id: Product.Id) {
        val oldProductId = productId.value
        productId.value = id
        if (id != oldProductId) {
            clearSelectedProductSizeAndHeight()
        }
    }

    suspend fun awaitProduct(): ProductDetailed? {
        val successResult = productResult.firstOrNull { it?.isSuccess == true }
        return successResult?.getOrNull()
    }

    suspend fun fetchProduct() {
        if (fetchProductJob?.isActive == true) return
        val productId = requireProductId()
        fetchProductImpl(productId)
    }

    suspend fun fetchTotalLookProducts() {
        if (fetchTotalLookProductsJob?.isActive == true) return
        val productId = requireProductId()
        fetchTotalLookProductsImpl(productId)
    }

    suspend fun fetchSimilarProducts() {
        if (fetchSimilarProductsJob?.isActive == true) return
        val productId = requireProductId()
        fetchSimilarProductsImpl(productId)
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

    private fun initTotalLookProductsFetching() {
        _totalLookProductsResult.subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .onEach { isActive ->
                if (isActive) startTotalLookProductsFetching() else stopTotalLookProductsFetching()
            }
            .launchIn(coroutineScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startTotalLookProductsFetching() {
        totalLookProductsFetchingJob?.cancel()
        totalLookProductsFetchingJob = productId
            .filterNotNull()
            .distinctUntilChanged()
            .onEachLatest { id ->
                // TODO: [Top] Don't fetch if the result is already there
                fetchTotalLookProductsImpl(id)
            }
            .launchIn(coroutineScope)
    }

    private fun stopTotalLookProductsFetching() {
        totalLookProductsFetchingJob?.cancel()
    }

    private fun initSimilarProductsFetching() {
        _similarProductsResult.subscriptionCount
            .map { it > 0 }
            .distinctUntilChanged()
            .onEach { isActive ->
                if (isActive) startSimilarProductsFetching() else stopSimilarProductsFetching()
            }
            .launchIn(coroutineScope)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun startSimilarProductsFetching() {
        similarProductsFetchingJob?.cancel()
        similarProductsFetchingJob = productId
            .filterNotNull()
            .distinctUntilChanged()
            .onEachLatest { id ->
                // TODO: [Top] Don't fetch if the result is already there
                fetchSimilarProductsImpl(id)
            }
            .launchIn(coroutineScope)
    }

    private fun stopSimilarProductsFetching() {
        similarProductsFetchingJob?.cancel()
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

    private suspend fun fetchTotalLookProductsImpl(id: Product.Id) {
        if (fetchTotalLookProductsJob?.isActive == true) return
        coroutineScope {
            fetchTotalLookProductsJob = launch {
                operationTracker.track(TotalLookProductsRequest) {
                    val params = GetProductTotalLookUseCase.Params(id)
                    _totalLookProductsResult.value = getProductTotalLookUseCase(params)
                }
            }
        }
    }

    private suspend fun fetchSimilarProductsImpl(id: Product.Id) {
        if (fetchSimilarProductsJob?.isActive == true) return
        coroutineScope {
            fetchSimilarProductsJob = launch {
                operationTracker.track(SimilarProductsRequest) {
                    val params = GetSimilarProductsUseCase.Params(id)
                    _similarProductsResult.value = getSimilarProductsUseCase(params)
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

    private fun clearSelectedProductSizeAndHeight() {
        _selectedProductSize.value = null
        _selectedProductHeight.value = null
    }

    private fun List<ProductOffer>.toSizeToHeightsMap(): Map<String, List<String>> {
        val sizeToHeights = mutableMapOf<String, List<String>>()
        this.forEach { offer ->
            sizeToHeights.compute(offer.size) { _, currentHeights ->
                currentHeights?.plus(offer.size) ?: listOf(offer.size)
            }
        }
        return sizeToHeights.toMap()
    }

    // TODO: [High] Extract?
    private fun Flow<Result<ProductDetailed>?>.updateProductInternalState(): Flow<Result<ProductDetailed>?> {
        return combine(
            this,
            wishlistProductIds,
            cartProductIds,
        ) { productResult, wishlistProductIds, cartProductIds ->
            productResult?.map { product ->
                product.copy(
                    isInWishlist = product.id in wishlistProductIds,
                    isInCart = product.id in cartProductIds,
                )
            }
        }
    }

    // TODO: [High] Extract?
    private fun Flow<Result<List<ProductShort>>?>.updateProductListInternalState(): Flow<Result<List<ProductShort>>?> {
        return combine(
            this,
            wishlistProductIds,
            cartProductIds,
        ) { productResult, wishlistProductIds, cartProductIds ->
            productResult?.map { products ->
                products.map { product ->
                    product.copy(
                        isInWishlist = product.id in wishlistProductIds,
                        isInCart = product.id in cartProductIds,
                    )
                }
            }
        }
    }

    private data object ProductRequest : OperationKey

    private data object TotalLookProductsRequest : OperationKey

    private data object SimilarProductsRequest : OperationKey
}

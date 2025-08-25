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
import ru.livetyping.zarina.core.domain.model.product.ProductAiReviews
import ru.livetyping.zarina.core.domain.model.product.ProductDetailed
import ru.livetyping.zarina.core.domain.model.product.ProductHeight
import ru.livetyping.zarina.core.domain.model.product.ProductOffer
import ru.livetyping.zarina.core.domain.model.product.ProductShort
import ru.livetyping.zarina.core.domain.model.product.ProductSizeFull
import ru.livetyping.zarina.core.domain.usecase.cart.GetCartProductIdsFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.product.GetProductAiReviewsUseCase
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
    private val getProductAiReviewsUseCase: GetProductAiReviewsUseCase,
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
        .onEach { result ->
            val product = result?.getOrNull()
            if (product != null) {
                updateSelectedProductSizeAndHeight(product)
            }
        }
        .updateProductInternalState()
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val _productAiReviewsResult = MutableStateFlow<Result<ProductAiReviews>?>(null)
    val productAiReviewsResult: StateFlow<Result<ProductAiReviews>?> = _productAiReviewsResult
        .asStateFlow()

    val isProductLoading: Flow<Boolean> = operationTracker.isOperationOngoing(ProductRequest)

    private val _selectedProductSize = MutableStateFlow<ProductSizeFull?>(null)
    val selectedProductSize: StateFlow<ProductSizeFull?> = _selectedProductSize.asStateFlow()

    private val _selectedProductHeight = MutableStateFlow<ProductHeight?>(null)
    val selectedProductHeight: StateFlow<ProductHeight?> = _selectedProductHeight.asStateFlow()

    val shouldSelectProductHeight: StateFlow<Boolean> = combine(
        productResult,
        selectedProductSize,
    ) { productResult, selectedSize ->
        val product = productResult?.getOrNull()
        when {
            product == null -> false
            selectedSize != null -> {
                val sizeOffers = product.offers.filter { it.size == selectedSize }
                sizeOffers.size > 1
            }

            else -> {
                val sizeSet = mutableSetOf<ProductSizeFull>()
                product.offers.forEach { offer ->
                    if (!sizeSet.add(offer.size)) return@combine true
                }
                false
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = false,
    )

    val selectedProductOffer: Flow<ProductOffer?> = combine(
        productResult,
        selectedProductSize,
        selectedProductHeight,
        shouldSelectProductHeight,
    ) { productResult, selectedSize, selectedHeight, shouldSelectHeight ->
        val product = productResult?.getOrNull()
        when {
            product == null -> null
            shouldSelectHeight -> {
                product.offers.find { it.size == selectedSize && it.height == selectedHeight }
            }

            else -> {
                product.offers.find { it.size == selectedSize }
            }
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = null,
    )

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

    private var productIdTotalLookProductsFetchedFor: Product.Id? = null
    private var productIdSimilarProductsFetchedFor: Product.Id? = null

    init {
        initProductFetching()
        initTotalLookProductsFetching()
        initSimilarProductsFetching()
        initProductAiReviewsFetching()
    }

    fun setProductId(id: Product.Id): Boolean {
        val prevId = productId.value
        productId.value = id
        return id != prevId
    }

    suspend fun awaitProduct(): ProductDetailed? {
        val successResult = productResult.firstOrNull { it?.isSuccess == true }
        return successResult?.getOrNull()
    }

    fun setSelectedProductSize(size: ProductSizeFull) {
        _selectedProductSize.value = size
        val product = productResult.value?.getOrNull()
        if (product != null) {
            updateSelectedProductHeight(product)
        }
    }

    fun setSelectedProductHeight(height: ProductHeight) {
        _selectedProductHeight.value = height
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

    suspend fun getProductSizes(): List<ProductOffer>? {
        val product = awaitProduct()
        return product?.offers?.distinctBy { it.size }
    }

    suspend fun getProductHeights(): List<ProductOffer>? {
        val selectedSize = selectedProductSize.value ?: return null
        val product = awaitProduct()
        return product?.offers?.filter { it.size == selectedSize }
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

    private fun initProductAiReviewsFetching() {
        productId
            .filterNotNull()
            .onEach { fetchProductAiReviews(it.toGroupId()) }
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
                if (shouldFetchTotalLookProducts(id)) {
                    fetchTotalLookProductsImpl(id)
                }
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
                if (shouldFetchSimilarProducts(id)) {
                    fetchSimilarProductsImpl(id)
                }
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
                    productIdTotalLookProductsFetchedFor = id
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
                    productIdSimilarProductsFetchedFor = id
                }
            }
        }
    }

    private suspend fun fetchProductAiReviews(groupId: Product.GroupId) {
        _productAiReviewsResult.value = getProductAiReviewsUseCase(
            GetProductAiReviewsUseCase.Params(groupId)
        )
    }

    private fun shouldFetchProduct(id: Product.Id): Boolean {
        val currentProductResult = productResult.value
        val currentProduct = currentProductResult?.getOrNull()
        return currentProduct?.id != id
    }

    private fun shouldFetchTotalLookProducts(id: Product.Id): Boolean {
        return productIdTotalLookProductsFetchedFor != id
    }

    private fun shouldFetchSimilarProducts(id: Product.Id): Boolean {
        return productIdSimilarProductsFetchedFor != id
    }

    private fun requireProductId(): Product.Id {
        return checkNotNull(productId.value) {
            "productId.value is null. Did you forgot to call setProductId?"
        }
    }

    private fun updateSelectedProductSizeAndHeight(product: ProductDetailed) {
        updateSelectedProductSize(product)
        updateSelectedProductHeight(product)
    }

    private fun updateSelectedProductSize(product: ProductDetailed) {
        val currentSelectedSize = selectedProductSize.value
        if (currentSelectedSize != null) {
            val sizeMatch = product.offers.find { it.size == currentSelectedSize }
            if (sizeMatch != null) {
                _selectedProductSize.value = sizeMatch.size
            } else {
                _selectedProductSize.value = getDefaultProductSize(product)
            }
        } else {
            _selectedProductSize.value = getDefaultProductSize(product)
        }
    }

    private fun updateSelectedProductHeight(product: ProductDetailed) {
        val currentSelectedHeight = selectedProductHeight.value
        if (currentSelectedHeight != null) {
            val heightMatch = product.offers.find { it.height == currentSelectedHeight }
            if (heightMatch != null) {
                _selectedProductHeight.value = heightMatch.height
            } else {
                _selectedProductHeight.value = selectedProductSize.value?.let { size ->
                    getDefaultProductHeight(product, size)
                }
            }
        } else {
            _selectedProductHeight.value = selectedProductSize.value?.let { size ->
                getDefaultProductHeight(product, size)
            }
        }
    }

    private fun getDefaultProductSize(product: ProductDetailed): ProductSizeFull? {
        return product.offers.firstOrNull()?.size
    }

    private fun getDefaultProductHeight(
        product: ProductDetailed,
        size: ProductSizeFull,
    ): ProductHeight? {
        return product.offers.find { it.size == size }?.height
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
package ru.livetyping.zarina.presentation.screen.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.domain.product.ProductItem
import ru.livetyping.zarina.presentation.base.text.Text
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.common.util.library.paging.mapProducts
import ru.livetyping.zarina.presentation.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.presentation.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.presentation.screen.favorites.FavoritesViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import timber.log.Timber

@HiltViewModel(assistedFactory = FavoritesViewModel.Factory::class)
class FavoritesViewModel @AssistedInject constructor(
    @Assisted
    private val sizeSelectorResultFlow: StateFlow<SizeSelectorGraph.Result?>,
    savedStateHandle: SavedStateHandle,
    private val interactor: FavoritesInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var clearFavoriteProductsJob: Job? = null

    private val favoriteProductsRequester = FlowRequester(FavoriteProductsRequest) {
        interactor.favoriteProductPager.getFavoriteProductPagingDataFlow()
    }

    val isClearFavoritesButtonVisible: StateFlow<Boolean> = interactor.getFavoriteProductIdsFlow()
        .map { result ->
            val favoriteProductIds = result.getOrNull()
            !favoriteProductIds.isNullOrEmpty()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = false,
        )

    val productPagingDataFlow: Flow<PagingData<ProductItem>> = favoriteProductsRequester.flow
        .cachedIn(viewModelScopeDefault)
        .mapProducts(
            favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCartProductIdsFlow(),
        )
        .cachedIn(viewModelScopeDefault)

    init {
        handleSizeSelectorResult()
    }

    fun onScreenCreated() {
        favoriteProductsRequester.request(FavoriteProductsRequest)
        viewModelScope.launch {
            interactor.fetchFavoriteProductIds()
        }
    }

    fun onProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = FavoritesScreenAction.ProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        viewModelScope.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.id)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess { isProductInFavorites ->
                    if (isProductInFavorites) {
                        val text = Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(text)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                        AppMetricaHelper.reportProductAddedToWishlist(product)
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val message = ZarinaToastMessage.error(Text.Resource(messageResId))
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onAddProductToCartClicked(product: Product) {
        if (product.offers.size > 1) {
            navigationThrottler.throttle {
                val action = FavoritesScreenAction.AddProductToCartClicked(product)
                emitSideEffect(SideEffect.Navigate(action))
            }
        } else {
            val offer = product.offers.firstOrNull() ?: run {
                Timber.e("Could not add product $product to cart because it has no offers")
                return
            }
            addProductToCart(product, offer.barcode)
        }
    }

    fun onSubscribeToProductClicked(product: Product) {
        navigationThrottler.throttle {
            val action = FavoritesScreenAction.SubscribeToProductClicked(product)
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onClearFavoritesClicked() {
        if (clearFavoriteProductsJob?.isActive == true) return

        clearFavoriteProductsJob = viewModelScope.launch {
            interactor.clearFavoriteProducts()
                .onSuccess {
                    favoriteProductsRequester.request(FavoriteProductsRequest)
                }
                .onFailure {
                    val text = Text.Resource(R.string.favorites_clearing_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = FavoritesScreenAction.GoToCatalogClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun addProductToCart(product: Product, barcode: Barcode) {
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                product = product,
                barcode = barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onSuccess {
                    val text = Text.Resource(R.string.product_adding_to_cart_completed)
                    val message = ZarinaToastMessage(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val text = Text.Resource(R.string.product_adding_to_cart_error)
                    val message = ZarinaToastMessage.error(text)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
        }
    }

    private fun handleSizeSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<SizeSelectorGraph.Result>(
                resultFlow = sizeSelectorResultFlow,
                key = KEY_SIZE_SELECTOR_RESULT,
            ) { result ->
                addProductToCart(
                    product = result.product.toProductItem(),
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: FavoritesScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect
    }

    @AssistedFactory
    interface Factory {
        fun create(
            sizeSelectorResultFlow: StateFlow<SizeSelectorGraph.Result?>,
        ): FavoritesViewModel
    }

    private data object FavoriteProductsRequest : FlowRequester.Request

    companion object {
        private const val KEY_SIZE_SELECTOR_RESULT = "size_selector_result"
    }
}

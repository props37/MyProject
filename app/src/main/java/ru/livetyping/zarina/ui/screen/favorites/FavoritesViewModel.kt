package ru.livetyping.zarina.ui.screen.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.R
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.common.Barcode
import ru.livetyping.zarina.domain.product.Product
import ru.livetyping.zarina.ui.base.text.Text
import ru.livetyping.zarina.ui.common.util.ScreenResultHandler
import ru.livetyping.zarina.ui.common.util.getNavigationThrottler
import ru.livetyping.zarina.ui.common.util.library.paging.mapProducts
import ru.livetyping.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.livetyping.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.livetyping.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect
import ru.livetyping.zarina.usecase.cart.AddProductToCartUseCase
import ru.livetyping.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import timber.log.Timber

@HiltViewModel(assistedFactory = FavoritesViewModel.Factory::class)
class FavoritesViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    savedStateHandle: SavedStateHandle,
    private val interactor: FavoritesInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private var clearFavoriteProductsJob: Job? = null

    private val favoriteProductFetchRequests = Channel<Unit>(Channel.CONFLATED)

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

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingDataFlow: Flow<PagingData<Product>> = favoriteProductFetchRequests.receiveAsFlow()
        .flatMapLatest {
            interactor.getFavoriteProductPagingDataFlow()
        }
        .cachedIn(viewModelScope)
        .mapProducts(
            favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCartProductIdsFlow(),
        )
        .cachedIn(viewModelScope)

    init {
        handleSizeSelectorResult()
    }

    fun onScreenCreated() {
        favoriteProductFetchRequests.trySend(Unit)
    }

    fun onProductClicked(product: Product) {
        // TODO: [High] Implement
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        viewModelScope.launch {
            val params = ToggleProductPresenceInFavoritesUseCase.Params(product.id)
            interactor.toggleProductPresenceInFavorites(params)
                .onSuccess {
                    if (!product.isInFavorites) {
                        val messageText =
                            Text.Resource(R.string.product_adding_to_favorites_completed)
                        val message = ZarinaToastMessage(messageText)
                        emitSideEffect(SideEffect.ShowZarinaToast(message))
                    }
                }
                .onFailure {
                    val messageResId = if (product.isInFavorites) {
                        R.string.product_removing_from_favorites_error
                    } else {
                        R.string.product_adding_to_favorites_error
                    }
                    val message = Text.Resource(messageResId)
                    emitSideEffect(SideEffect.ShowToast(message))
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
            addProductToCart(product.id, offer.barcode)
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
                    favoriteProductFetchRequests.trySend(Unit)
                }
                .onFailure {
                    val message = Text.Resource(R.string.favorites_clearing_error)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    fun onGoToCatalogClicked() {
        navigationThrottler.throttle {
            val action = FavoritesScreenAction.GoToCatalogClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    private fun addProductToCart(productId: Product.Id, barcode: Barcode) {
        viewModelScope.launch {
            val params = AddProductToCartUseCase.Params(
                productId = productId,
                barcode = barcode,
                count = 1,
            )
            interactor.addProductToCart(params)
                .onSuccess {
                    val messageText = Text.Resource(R.string.product_adding_to_cart_completed)
                    val message = ZarinaToastMessage(messageText)
                    emitSideEffect(SideEffect.ShowZarinaToast(message))
                }
                .onFailure {
                    val message = Text.Resource(R.string.product_adding_to_cart_error)
                    emitSideEffect(SideEffect.ShowToast(message))
                }
        }
    }

    private fun handleSizeSelectorResult() {
        viewModelScope.launch {
            screenResultHandler.handle<SizeSelectorGraph.Result>(
                key = SizeSelectorGraph.RESULT_KEY,
            ) { result ->
                addProductToCart(
                    productId = result.product.toProduct().id,
                    barcode = result.offer.toProductOffer().barcode,
                )
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: FavoritesScreenAction) : SideEffect

        data class ShowZarinaToast(val message: ZarinaToastMessage) : SideEffect

        data class ShowToast(val message: Text) : SideEffect
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): FavoritesViewModel
    }
}

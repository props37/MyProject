package ru.zarina.zarina.ui.screen.favorites

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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.common.Barcode
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.base.text.Text
import ru.zarina.zarina.ui.common.util.ScreenResultHandler
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.common.util.library.paging.mapProducts
import ru.zarina.zarina.ui.common.zarinatoast.ZarinaToastMessage
import ru.zarina.zarina.ui.navigation.destination.graph.SizeSelectorGraph
import ru.zarina.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect
import ru.zarina.zarina.usecase.cart.AddProductToCartUseCase
import ru.zarina.zarina.usecase.favorite.ToggleProductPresenceInFavoritesUseCase
import ru.zarina.zarina.util.base.usecase.invoke
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

    private val favoriteProductFetchRequests = Channel<Unit>(Channel.CONFLATED)

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
        // TODO: [High] Implement
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

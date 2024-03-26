package ru.zarina.zarina.ui.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.common.util.library.paging.mapProducts
import ru.zarina.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect
import ru.zarina.zarina.util.base.usecase.invoke
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val interactor: FavoritesInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val productPagingDataFlow: Flow<PagingData<Product>> = interactor.getFavoriteProductPagingDataFlow()
        .cachedIn(viewModelScope)
        .mapProducts(
            favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCartProductIdsFlow(),
        )
        .cachedIn(viewModelScope)

    fun onProductClicked(product: Product) {
        // TODO: [High] Implement
    }

    fun onAddProductToFavoritesClicked(product: Product) {
        // TODO: [High] Implement
    }

    fun onAddProductToCartClicked(product: Product) {
        // TODO: [High] Implement
    }

    fun onSubscribeToProductClicked(product: Product) {
        // TODO: [High] Implement
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: FavoritesScreenAction) : SideEffect
    }
}

package ru.zarina.zarina.ui.screen.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.domain.product.Product
import ru.zarina.zarina.ui.common.util.library.paging.mapProducts
import ru.zarina.zarina.ui.screen.favorites.FavoritesViewModel.SideEffect
import ru.zarina.zarina.util.base.usecase.invoke
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val interactor: FavoritesInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    val productPagingDataFlow: Flow<PagingData<Product>> = interactor.getFavoriteProductPagingDataFlow()
        .cachedIn(viewModelScope)
        .mapProducts(
            favoriteProductIdsResultFlow = interactor.getFavoriteProductIdsFlow(),
            cartProductIdsResultFlow = interactor.getCartProductIdsFlow(),
        )
        .cachedIn(viewModelScope)

    sealed interface SideEffect : SideEffectSource.SideEffect
}

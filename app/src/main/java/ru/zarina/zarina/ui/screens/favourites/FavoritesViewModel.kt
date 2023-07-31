package ru.zarina.zarina.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.favourites.paging.FavouritesPagingSource
import javax.inject.Inject

@KoinViewModel
class FavoritesViewModel @Inject constructor(
    private val interactor: FavoritesInteractor,
) : ViewModel(),
    ISideEffectSource<FavoritesViewModel.SideEffect> by SideEffectQueue() {

    private val pagingSource = MutableStateFlow<PagingSource<Int, Product>?>(null)
    private val pager = Pager(
        config = PagingConfig(
            pageSize = PAGE_SIZE,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = {
            val source = FavouritesPagingSource(interactor.getFavoritesPageUseCase)
            pagingSource.value = source
            source
        },
    )
    val favorites = pager.flow
        .cachedIn(viewModelScope)
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product))
    }

    fun onFavoriteChange(product: Product, isFavorite: Boolean) {
        viewModelScope.launch {
            interactor.setIsFavorite(product, isFavorite)
                .onSuccess { pagingSource.value?.invalidate() }
                .onFailure {
                    // TODO shake heart
                }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowProduct(val product: Product) : SideEffect
    }

    companion object {
        private const val PAGE_SIZE = 12
    }

}

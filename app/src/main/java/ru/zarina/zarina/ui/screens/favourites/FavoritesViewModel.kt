package ru.zarina.zarina.ui.screens.favourites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.cachedIn
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.screens.favourites.paging.FavouritesPagingSource
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

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

    private val _shakingFavorites = MutableStateFlow<Set<Product.Id>>(emptySet())
    val shakingFavorites = _shakingFavorites.mapState(viewModelScope) { it.toPersistentSet() }

    init {
        interactor.getFavoriteIds()
            .distinctUntilChanged()
            .onEach { pagingSource.value?.invalidate() }
            .launchIn(viewModelScope)
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product))
    }

    fun onFavoriteChange(product: Product, isFavorite: Boolean) {
        viewModelScope.launch {
            interactor.setIsFavorite(product, isFavorite)
                .onFailure {
                    _shakingFavorites.update { it + product.id }
                    delay(FAVORITE_SHAKE_DURATION)
                    _shakingFavorites.update { it - product.id }
                }
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowProduct(val product: Product) : SideEffect
    }

    companion object {
        private const val PAGE_SIZE = 12

        private val FAVORITE_SHAKE_DURATION = 1.seconds
    }

}

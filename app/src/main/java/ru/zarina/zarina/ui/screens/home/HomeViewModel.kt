package ru.zarina.zarina.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.datasource.cache.Cache
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.domain.Action
import ru.zarina.zarina.domain.Banner
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.Url
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.utils.coroutine.mapState

@KoinViewModel
class HomeViewModel(
    private val interactor: HomeInteractor,
    cache: Cache,
) : ViewModel(),
    ISideEffectSource<HomeViewModel.SideEffect> by SideEffectQueue() {

    val cache = MutableStateFlow(cache).asStateFlow()

    private val _banners = MutableStateFlow<Result<List<Banner>>?>(null)
    val banners = _banners
        .map { it?.getOrNull().orEmpty().toPersistentList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())

    private val selectionsResult = interactor.getSelections()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)
    val selections = selectionsResult
        .map { it?.getOrNull().orEmpty().toPersistentList() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), persistentListOf())

    private val _shakingFavorites = MutableStateFlow(emptySet<Product.Id>())
    val shakingFavorites = _shakingFavorites.mapState(viewModelScope) { it.toPersistentSet() }

    // TODO add error display

    init {
        loadBanners()
    }

    private fun loadBanners() = viewModelScope.launch {
        _banners.value = interactor.getBanners()
    }

    fun onBannerClick(banner: Banner) {
        val effect = when (banner.action) {
            is Action.Link -> SideEffect.ShowWebpage(banner.action.url)
            is Action.Product -> SideEffect.ShowProduct(banner.action.id)
            is Action.Products -> SideEffect.ShowProducts(
                categoryId = banner.action.categoryId,
                filtration = banner.action.filtration,
            )

            else -> null
        }
        effect?.let { sideEffect(it) }
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product.id))
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowProduct(val productId: Product.Id) : SideEffect
        data class ShowWebpage(val url: Url) : SideEffect
        data class ShowProducts(
            val categoryId: Category.Id,
            val filtration: Filtration?
        ) : SideEffect
    }

}

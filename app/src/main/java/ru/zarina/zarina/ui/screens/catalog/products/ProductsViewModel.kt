package ru.zarina.zarina.ui.screens.catalog.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.PluralManager
import ru.zarina.zarina.ui.common.base.PluralResources
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.products.paging.CategoryProductPagingSource
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val interactor: ProductsInteractor,
) : ViewModel(),
    ISideEffectSource<ProductsViewModel.SideEffect> by SideEffectQueue() {

    private val categoryId = savedStateHandle
        .getStateFlow<Int?>(Catalog.Products.ARGUMENT_CATEGORY_ID, null)
        .mapState(viewModelScope) { id -> id?.let { Category.Id(it) } }

    @OptIn(ExperimentalCoroutinesApi::class)
    val category = categoryId
        .flatMapLatest { id -> id?.let { interactor.getCategory(it) } ?: flowOf(null) }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    private val pagingSource = category
        .mapState(viewModelScope) { category ->
            category?.let { CategoryProductPagingSource(it, interactor.getProductsPageUseCase) }
        }

    private val productsPluralManager = PluralManager(
        PluralResources(
            zero = R.string.plural_products_zero,
            one = R.string.plural_products_one,
            two = R.string.plural_products_two,
            few = R.string.plural_products_few,
            many = R.string.plural_products_many,
            other = R.string.plural_products_other
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    val productCount = pagingSource
        .flatMapLatest { it?.itemCount ?: flowOf(null) }
        .map { count ->
            when (count) {
                null -> null
                0 -> Text.Resource(R.string.no_products)
                else -> productsPluralManager.getText(count, count)
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    val pager = pagingSource
        .mapState(viewModelScope) { source ->
            if (source == null) return@mapState null
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    enablePlaceholders = false,
                ),
                pagingSourceFactory = { source },
            )
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    val products = pager.flatMapLatest {
        it?.flow?.cachedIn(viewModelScope) ?: emptyFlow()
    }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product.id))
    }

    fun onSortClick() {
        sideEffect(SideEffect.ShowSelectSort)
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        data class ShowProduct(val id: Product.Id) : SideEffect
        object ShowSelectSort : SideEffect
    }

    companion object {
        private const val PAGE_SIZE = 12
    }

}

package ru.zarina.zarina.ui.screens.catalog.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.domain.Filtration
import ru.zarina.zarina.domain.Product
import ru.zarina.zarina.domain.ProductSort
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.PluralManager
import ru.zarina.zarina.ui.common.base.PluralResources
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.Text
import ru.zarina.zarina.ui.navigation.destinations.Catalog
import ru.zarina.zarina.ui.screens.catalog.products.paging.CachingCategoryProductPagingSource
import ru.zarina.zarina.ui.screens.catalog.products.paging.CategoryProductPagingSource
import ru.zarina.zarina.ui.screens.catalog.products.paging.EmptyProductPagingSource
import ru.zarina.zarina.utils.coroutine.mapState
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalCoroutinesApi::class)
class ProductsViewModel(
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

    val sort = savedStateHandle.getStateFlow(KEY_SELECTED_SORT, ProductSort.DEFAULT)

    /**
     * Default filtration used when no filtration was sent.
     */
    private val baseFiltration =
        savedStateHandle.getStateFlow<Filtration?>(KEY_BASE_FILTRATION, null)

    /**
     * Filtration that was applied to the products currently displayed.
     */
    private val appliedFiltration =
        savedStateHandle.getStateFlow<Filtration?>(KEY_APPLIED_FILTRATION, null)

    /**
     * The latest filtration that was requested by the user.
     */
    private val requestedFiltration =
        savedStateHandle.getStateFlow<Filtration?>(
            KEY_REQUESTED_FILTRATION,
            savedStateHandle[Catalog.Products.ARGUMENT_FILTRATION]
        )

    val isFilterButtonEnabled = requestedFiltration
        .map { it != null }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), false)

    private val pagingSourceFactory =
        combine(category, sort, requestedFiltration) { category, sort, filtration ->
            {
                category?.let {
                    CachingCategoryProductPagingSource(
                        category = it,
                        sort = sort,
                        filtration = filtration,
                        getProductsPageUseCase = interactor.getProductsPageUseCase
                    )
                } ?: EmptyProductPagingSource()
            }
        }
            .stateIn(viewModelScope, SharingStarted.Eagerly) { EmptyProductPagingSource() }

    private val pagingSource = MutableStateFlow<CategoryProductPagingSource?>(null)

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

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val productCount = pagingSource
        .flatMapLatest { it?.itemCount ?: flowOf(null) }
        .map { count ->
            when (count) {
                null -> null
                0 -> Text.Resource(R.string.no_products)
                else -> productsPluralManager.getText(count, count)
            }
        }
        .debounce(250.milliseconds)
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    private val pager = MutableStateFlow(
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false,
            ),
            pagingSourceFactory = {
                val source = pagingSourceFactory.value()
                pagingSource.value = source
                source
            },
        )
    )

    val products = pager.flatMapLatest {
        it.flow.cachedIn(viewModelScope)
    }
        .shareIn(viewModelScope, SharingStarted.Eagerly, replay = 1)

    init {
        pagingSource
            .flatMapLatest { it?.appliedFiltration ?: emptyFlow() }
            .filterNotNull()
            .onEach {
                savedStateHandle[KEY_APPLIED_FILTRATION] = it
                if (baseFiltration.value == null)
                    savedStateHandle[KEY_BASE_FILTRATION] = it
                if (requestedFiltration.value == null)
                    savedStateHandle[KEY_REQUESTED_FILTRATION] = it
            }
            .launchIn(viewModelScope)

        interactor.getFavoriteIds()
            .distinctUntilChanged()
            .onEach { pagingSource.value?.invalidate() }
            .launchIn(viewModelScope)
    }

    fun onBackClick() {
        sideEffect(SideEffect.GoBack)
    }

    fun onProductClick(product: Product) {
        sideEffect(SideEffect.ShowProduct(product.id))
    }

    fun onSortClick() {
        sideEffect(SideEffect.ShowSelectSort)
    }

    fun onFiltersClick() {
        sideEffect(SideEffect.ShowFilters)
    }

    fun onFavoriteChange(product: Product, isFavorite: Boolean) {
        viewModelScope.launch {
            interactor.setIsFavorite(product, isFavorite)
            // TODO shake the heart button on failure
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        object GoBack : SideEffect
        data class ShowProduct(val id: Product.Id) : SideEffect
        object ShowSelectSort : SideEffect
        object ShowFilters : SideEffect
    }

    companion object {
        private const val PAGE_SIZE = 12

        const val KEY_SELECTED_SORT = "selected_sort"
        const val KEY_BASE_FILTRATION = "base_filtration"
        const val KEY_APPLIED_FILTRATION = "applied_filtration"
        const val KEY_REQUESTED_FILTRATION = "requested_filtration"
    }

}

package ru.zarina.zarina.ui.screen.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.domain.rework.product.Product
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.common.SortingParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
import ru.zarina.zarina.usecase.rework.product.GetProductPagingDataFlowUseCase
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import javax.inject.Inject

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductsInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val categoryId: StateFlow<Category.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = UnscopedDestinations.Products.ARG_KEY_CATEGORY_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "categoryId is null" }
            Category.Id(value)
        }

    private val categoryFetchRequests = MutableSharedFlow<Unit>(replay = 1)
        .also { it.tryEmit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryResult: StateFlow<Result<Category>?> = combine(
        categoryId,
        categoryFetchRequests,
    ) { id, _ -> id }
        .flatMapLatest { id ->
            interactor.getCategoryFlow(GetCategoryFlowUseCase.Params(id))
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val category: StateFlow<Category?> = categoryResult
        .map { result ->
            result?.getOrNull()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
            initialValue = null,
        )

    private val currentSorting: StateFlow<Sorting> = savedStateHandle
        .getStateFlow<SortingParcelable?>(
            key = KEY_CURRENT_SORTING,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toSorting() ?: Sorting.getDefault() }

    @OptIn(ExperimentalCoroutinesApi::class)
    val productPagingDataFlow: Flow<PagingData<Product>> = combine(
        categoryId,
        currentSorting,
    ) { categoryId, sorting ->
        GetProductPagingDataFlowUseCase.Params(categoryId, sorting)
    }
        .flatMapLatest { params ->
            interactor.getProductPagingDataFlow(params)
        }
        .cachedIn(viewModelScope)

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward)
        }
    }

    fun onSearchClicked() {
        // TODO: [High] Implement
    }

    fun onFiltersClicked() {
        navigationThrottler.throttle {
            val action = ProductsScreenAction.FiltersClicked(categoryId.value)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    fun onRefreshProducts() {
        if (category.value == null) {
            fetchCategory()
        }
    }

    fun onProductsErrorRefreshClicked() {
        if (category.value == null) {
            fetchCategory()
        }
    }

    private fun fetchCategory() {
        categoryFetchRequests.tryEmit(Unit)
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: ProductsScreenAction) : SideEffect
        data object NavigateBackward : SideEffect
    }

    companion object {
        private const val KEY_CURRENT_SORTING = "current_sorting"
    }
}


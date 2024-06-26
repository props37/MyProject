package ru.livetyping.zarina.presentation.screen.products.filters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.filter.Filter
import ru.livetyping.zarina.domain.filter.Filters
import ru.livetyping.zarina.domain.filter.ListFilter
import ru.livetyping.zarina.domain.filter.coerceInAvailable
import ru.livetyping.zarina.domain.filter.reset
import ru.livetyping.zarina.domain.filter.updateWith
import ru.livetyping.zarina.domain.product.CategoryProductInfo
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.screenresult.ScreenResultHandler
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.presentation.model.filter.FiltersParcelable
import ru.livetyping.zarina.presentation.navigation.destination.UnscopedDestinations
import ru.livetyping.zarina.presentation.screen.filters.FilterListState
import ru.livetyping.zarina.presentation.screen.products.filters.ProductFiltersViewModel.SideEffect
import ru.livetyping.zarina.usecase.product.GetCategoryProductInfoFlowUseCase
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import ru.livetyping.zarina.util.library.coroutines.mapState
import timber.log.Timber

// TODO: [High] DRY!

@HiltViewModel(assistedFactory = ProductFiltersViewModel.Factory::class)
class ProductFiltersViewModel @AssistedInject constructor(
    @Assisted
    private val listFilterResultFlow: StateFlow<UnscopedDestinations.ListFilter.Result?>,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: ProductFiltersInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(savedStateHandle)

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val categoryId: StateFlow<Category.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = UnscopedDestinations.ProductFilters.ARG_KEY_CATEGORY_ID,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { value ->
            checkNotNull(value) { "categoryId is null" }
            Category.Id(value)
        }

    private val initialFilters: StateFlow<Filters?> = savedStateHandle
        .getStateFlow<FiltersParcelable?>(
            key = UnscopedDestinations.ProductFilters.ARG_KEY_FILTERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { it?.toFilters() }

    private val filters: StateFlow<Filters?> = savedStateHandle
        .getStateFlow<FiltersParcelable?>(
            key = KEY_FILTERS,
            initialValue = null,
        )
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
        ) { parcelable ->
            parcelable?.toFilters() ?: initialFilters.value
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryProductInfoRequester = FlowRequester(CategoryProductInfoRequest.GENERAL) {
        combine(categoryId, filters) { categoryId, filters ->
            val params = GetCategoryProductInfoFlowUseCase.Params(categoryId, filters)
            interactor.getCategoryProductInfoFlow(params)
        }
            .flatMapLatest { it }
    }

    private val categoryProductInfoResult: StateFlow<Result<CategoryProductInfo>?> =
        categoryProductInfoRequester.flow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = null,
            )

    val filterListState: StateFlow<FilterListState> = combine(
        filters,
        categoryProductInfoResult,
    ) { filters, categoryProductInfoResult ->
        if (filters != null) {
            val availableFilters = categoryProductInfoResult?.getOrNull()?.availableFilters
            val combinedFilters = availableFilters?.let { filters.coerceInAvailable(it) } ?: filters
            FilterListState.FilterList(combinedFilters)
        } else {
            categoryProductInfoResult?.fold(
                onSuccess = { info ->
                    FilterListState.FilterList(info.availableFilters)
                },
                onFailure = { throwable ->
                    val errorState = ErrorState.from(throwable)
                    FilterListState.Error(errorState)
                },
            ) ?: FilterListState.Loading
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = filters.value?.let { filters ->
            FilterListState.FilterList(filters)
        } ?: FilterListState.Loading,
    )

    val isPickupStoresFilterVisible: StateFlow<Boolean> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { filters ->
        filters?.storePickupAvailability?.isEnabled == true
    }

    val isResetFiltersButtonVisible: StateFlow<Boolean> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it?.hasAppliedIgnoringSorting == true }

    init {
        handleListFilterResult()
    }

    val productCount: StateFlow<Int?> = categoryProductInfoResult
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { it?.getOrNull()?.productCount }

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward(ProductFiltersScreenResult.ScreenClosed))
        }
    }

    fun onResetFiltersClicked() {
        val filters = filters.value
        if (filters != null) {
            val newFilters = filters.reset()
            savedStateHandle[KEY_FILTERS] = FiltersParcelable.from(newFilters)
        } else {
            Timber.w("Could not reset filters since it is null")
        }
    }

    fun onFilterChanged(filter: Filter) {
        val newFilters = filters.value?.updateWith(filter)
        savedStateHandle[KEY_FILTERS] = newFilters?.let { FiltersParcelable.from(it) }
    }

    fun onFilterClicked(filter: Filter) {
        if (filter is ListFilter<*>) {
            navigationThrottler.throttle {
                val action = ProductFiltersScreenAction.ListFilterClicked(filter)
                emitSideEffect(SideEffect.NavigateForward(action))
            }
        }
    }

    fun onShowProductsClicked() {
        navigationThrottler.throttle {
            val filters = filters.value
            val result = if (filters != null) {
                ProductFiltersScreenResult.FiltersChanged(filters)
            } else {
                ProductFiltersScreenResult.ScreenClosed
            }
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onFilterListErrorRefreshClicked() {
        categoryProductInfoRequester.request(CategoryProductInfoRequest.GENERAL)
    }

    private fun handleListFilterResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.ListFilter.Result>(
                resultFlow = listFilterResultFlow,
                key = KEY_RESULT_LIST_FILTER,
            ) { result ->
                val filter = result.filter.toListFilter()
                val newFilters = filters.value?.updateWith(filter)
                savedStateHandle[KEY_FILTERS] = newFilters?.let { FiltersParcelable.from(it) }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: ProductFiltersScreenAction) : SideEffect

        data class NavigateBackward(val result: ProductFiltersScreenResult) : SideEffect
    }

    private enum class CategoryProductInfoRequest : FlowRequester.Request { GENERAL }

    @AssistedFactory
    interface Factory {
        fun create(
            listFilterResultFlow: StateFlow<UnscopedDestinations.ListFilter.Result?>,
        ): ProductFiltersViewModel
    }

    companion object {
        private const val KEY_FILTERS = "filters"
        private const val KEY_RESULT_LIST_FILTER = "result_list_filter"
    }
}

package ru.zarina.zarina.ui.screen.filters

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.base.throttler.Throttler
import ru.zarina.zarina.domain.category.Category
import ru.zarina.zarina.domain.filter.Filter
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.domain.filter.ListFilter
import ru.zarina.zarina.domain.filter.coerceInAvailable
import ru.zarina.zarina.domain.filter.reset
import ru.zarina.zarina.domain.filter.updateWith
import ru.zarina.zarina.domain.product.CategoryProductInfo
import ru.zarina.zarina.ui.base.ErrorState
import ru.zarina.zarina.ui.base.from
import ru.zarina.zarina.ui.common.util.ScreenResultHandler
import ru.zarina.zarina.ui.common.util.getNavigationThrottler
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.navigation.destination.UnscopedDestinations
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.SideEffect
import ru.zarina.zarina.usecase.product.GetCategoryProductInfoFlowUseCase
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import timber.log.Timber

@HiltViewModel(assistedFactory = FiltersViewModel.Factory::class)
class FiltersViewModel @AssistedInject constructor(
    @Assisted
    backStackEntrySavedStateHandle: SavedStateHandle,
    private val savedStateHandle: SavedStateHandle,
    private val interactor: FiltersInteractor,
) : ViewModel(), SideEffectSource<SideEffect> by SideEffectSourceImpl() {

    private val screenResultHandler = ScreenResultHandler(
        backStackEntrySavedStateHandle = backStackEntrySavedStateHandle,
        savedStateHandle = savedStateHandle,
    )

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val categoryId: StateFlow<Category.Id> = savedStateHandle
        .getStateFlow<Long?>(
            key = UnscopedDestinations.Filters.ARG_KEY_CATEGORY_ID,
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
            key = UnscopedDestinations.Filters.ARG_KEY_FILTERS,
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

    private val categoryProductInfoFetchRequests = Channel<Unit>(Channel.CONFLATED)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryProductInfoResult: StateFlow<Result<CategoryProductInfo>?> = combine(
        categoryId,
        filters,
        categoryProductInfoFetchRequests.receiveAsFlow(),
    ) { categoryId, filters, _ ->
        GetCategoryProductInfoFlowUseCase.Params(categoryId, filters)
    }
        .flatMapLatest { params ->
            interactor.getCategoryProductInfoFlow(params)
        }
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

    val isResetButtonVisible: StateFlow<Boolean> = filters.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileUiSubscribed,
    ) { it?.isEmptyIgnoringSorting != true }

    init {
        categoryProductInfoFetchRequests.trySend(Unit)

        handleListFilterResult()
    }

    val productCount: StateFlow<Int?> = categoryProductInfoResult
        .mapState(
            scope = viewModelScope,
            started = SharingStarted.WhileUiSubscribed,
        ) { it?.getOrNull()?.productCount }

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward(FiltersScreenResult.ScreenClosed))
        }
    }

    fun onResetClicked() {
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
                val action = FiltersScreenAction.ListFilterClicked(filter)
                emitSideEffect(SideEffect.NavigateForward(action))
            }
        }
    }

    fun onShowProductsClicked() {
        navigationThrottler.throttle {
            val filters = filters.value
            val result = if (filters != null) {
                FiltersScreenResult.FiltersChanged(filters)
            } else {
                FiltersScreenResult.ScreenClosed
            }
            emitSideEffect(SideEffect.NavigateBackward(result))
        }
    }

    fun onFilterListErrorRefreshClicked() {
        categoryProductInfoFetchRequests.trySend(Unit)
    }

    private fun handleListFilterResult() {
        viewModelScope.launch {
            screenResultHandler.handle<UnscopedDestinations.ListFilter.Result>(
                key = UnscopedDestinations.ListFilter.RESULT_KEY,
            ) { result ->
                val filter = result.filter.toListFilter()
                val newFilters = filters.value?.updateWith(filter)
                savedStateHandle[KEY_FILTERS] = newFilters?.let { FiltersParcelable.from(it) }
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: FiltersScreenAction) : SideEffect

        data class NavigateBackward(val result: FiltersScreenResult) : SideEffect
    }

    @Stable
    sealed class FilterListState {
        data object Loading : FilterListState()

        data class FilterList(val filters: Filters) : FilterListState()

        data class Error(val errorState: ErrorState) : FilterListState()
    }

    @AssistedFactory
    interface Factory {
        fun create(backStackEntrySavedStateHandle: SavedStateHandle): FiltersViewModel
    }

    companion object {
        private const val KEY_FILTERS = "filters"
    }
}

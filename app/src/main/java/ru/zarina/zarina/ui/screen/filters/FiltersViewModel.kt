package ru.zarina.zarina.ui.screen.filters

import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.filter.Filters
import ru.zarina.zarina.domain.rework.filter.coerceIn
import ru.zarina.zarina.domain.rework.product.CategoryProductInfo
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.filter.FiltersParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.usecase.rework.product.GetCategoryProductInfoFlowUseCase
import ru.zarina.zarina.util.library.coroutines.WhileUiSubscribed
import ru.zarina.zarina.util.library.coroutines.mapState
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FiltersViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: FiltersInteractor,
) : ViewModel(), SideEffectSource<FiltersViewModel.SideEffect> by SideEffectSourceImpl() {

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

    private val filters = MutableStateFlow(initialFilters.value)

    private val categoryProductInfoFetchRequests = MutableSharedFlow<Unit>(replay = 1)
        .also { it.tryEmit(Unit) }

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoryProductInfoResult: StateFlow<Result<CategoryProductInfo>?> = combine(
        categoryId,
        categoryProductInfoFetchRequests,
    ) { categoryId, _ -> categoryId }
        .flatMapLatest { categoryId ->
            val params = GetCategoryProductInfoFlowUseCase.Params(categoryId)
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
            val resultFilters = availableFilters?.let { filters.coerceIn(it) } ?: filters
            FilterListState.FilterList(resultFilters)
        } else {
            categoryProductInfoResult?.fold(
                onSuccess = { info ->
                    FilterListState.FilterList(info.availableFilters)
                },
                onFailure = { throwable ->
                    val errorState = when (throwable) {
                        is IOException -> ErrorStateRework.NETWORK
                        else -> ErrorStateRework.GENERIC
                    }
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

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward(FiltersScreenResult.ScreenClosed))
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateBackward(val result: FiltersScreenResult) : SideEffect
    }

    @Stable
    sealed class FilterListState {
        data object Loading : FilterListState()

        data class FilterList(val filters: Filters) : FilterListState()

        data class Error(val errorState: ErrorStateRework) : FilterListState()
    }
}

package ru.zarina.zarina.ui.screen.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.common.Sorting
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.ui.model.common.SortingParcelable
import ru.zarina.zarina.ui.navigation.rework.graph.UnscopedDestinations
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect
import ru.zarina.zarina.usecase.rework.category.GetCategoryFlowUseCase
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

    @OptIn(ExperimentalCoroutinesApi::class)
    val category: StateFlow<Category?> = categoryId
        .flatMapLatest { id ->
            interactor.getCategoryFlow(GetCategoryFlowUseCase.Params(id))
                .map { result -> result.getOrNull() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
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
        ) { it?.toSorting() ?: Sorting.NEW }

    fun onBackClicked() {
        navigationThrottler.throttle {
            emitSideEffect(SideEffect.NavigateBackward)
        }
    }

    fun onSearchClicked() {
        // TODO: [High] Implement
    }

    fun onFiltersClicked() {
        // TODO: [High] Implement
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data object NavigateBackward : SideEffect
    }

    companion object {
        private const val KEY_CURRENT_SORTING = "current_sorting"
    }
}


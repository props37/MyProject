package ru.livetyping.zarina.feature.productlist.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.domain.cache.CachePolicy
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoryFlowUseCase
import ru.livetyping.zarina.core.uicommon.Throttler
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.feature.productlist.ui.api.ProductListNavEntry
import javax.inject.Inject

@HiltViewModel
internal class ProductListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    getCategoryFlow: GetCategoryFlowUseCase,
) : ViewModel(), SideEffectSource<ProductListSideEffect> by SideEffectSourceImpl() {

    // TODO: [Top] Inject dispatcher
    private val viewModelScopeDefault = viewModelScope + Dispatchers.Default

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val navEntry = savedStateHandle.toRoute<ProductListNavEntry>(
        typeMap = ProductListNavEntry.typeMap(),
    )
    private val categoryId = Category.Id(navEntry.categoryId)

    private val categoryRequester = FlowRequester(CategoryRequest) {
        val params = GetCategoryFlowUseCase.Params(
            id = categoryId,
            cachePolicy = CachePolicy.LocalFirstThenRemote(),
        )
        getCategoryFlow(params)
    }

    private val category = categoryRequester.flow
        .map { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    fun onBackClicked() {
        navigationThrottler.throttle {
            val action = ProductListScreenAction.BackClicked
            emitSideEffect(ProductListSideEffect.Navigate(action))
        }
    }

    private data object CategoryRequest : FlowRequest
}

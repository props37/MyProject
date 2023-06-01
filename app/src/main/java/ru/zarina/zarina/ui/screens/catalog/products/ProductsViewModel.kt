package ru.zarina.zarina.ui.screens.catalog.products

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
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

    val pagingSource = category
        .map { category ->
            category?.let { CategoryProductPagingSource(it, interactor.getProductsPageUseCase) }
        }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}

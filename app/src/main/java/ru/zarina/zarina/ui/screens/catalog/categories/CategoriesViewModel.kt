package ru.zarina.zarina.ui.screens.catalog.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.base.operation.OperationKey
import ru.zarina.zarina.ui.common.base.operation.OperationTracker
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val interactor: CategoriesInteractor,
) : ViewModel(),
    ISideEffectSource<CategoriesViewModel.SideEffect> by SideEffectQueue() {

    private val operationTracker = OperationTracker()

    private val categoriesResult = MutableStateFlow<Result<List<Category>>?>(null)
    val categories = categoriesResult
        .mapState(viewModelScope) { it?.getOrNull().orEmpty().toPersistentList() }
    val isLoaderVisible = operationTracker.isOperationOngoing(Operation.LOADING_CATEGORIES)
        .stateIn(viewModelScope, SharingStarted.Eagerly, true)

    init {
        viewModelScope.launch {
            operationTracker.track(Operation.LOADING_CATEGORIES) {
                // TODO errors
                categoriesResult.value = interactor.fetchCategories()
            }
        }
    }

    fun onCategoryClick(category: Category) {
        // TODO navigate
    }

    enum class Operation : OperationKey { LOADING_CATEGORIES }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}

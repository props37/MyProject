package ru.zarina.zarina.ui.screens.catalog.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import ru.zarina.zarina.R
import ru.zarina.zarina.base.operationtracker.OperationKey
import ru.zarina.zarina.base.operationtracker.OperationTracker
import ru.zarina.zarina.domain.old.Category
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.ui.common.text.Text
import ru.zarina.zarina.utils.coroutine.mapState

@KoinViewModel
class CategoriesViewModel(
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
                categoriesResult.value = interactor.fetchCategories()
                    .onFailure { sideEffect(SideEffect.ShowToast(Text.Resource(R.string.unable_to_load_categories))) }
            }
        }
    }

    fun onCategoryClick(category: Category) {
        sideEffect(SideEffect.ShowProducts(category.id))
    }

    fun onSearchClick() {
        sideEffect(SideEffect.ShowSearch)
    }

    enum class Operation : OperationKey { LOADING_CATEGORIES }

    sealed interface SideEffect : ISideEffectSource.ISideEffect {
        data class ShowProducts(val categoryId: Category.Id) : SideEffect
        data class ShowToast(val message: Text) : SideEffect
        object ShowSearch : SideEffect
    }

}

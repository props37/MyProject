package ru.zarina.zarina.ui.screens.catalog.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import ru.zarina.zarina.domain.Category
import ru.zarina.zarina.ui.common.base.ISideEffectSource
import ru.zarina.zarina.ui.common.base.SideEffectQueue
import ru.zarina.zarina.utils.coroutine.mapState
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val interactor: CategoriesInteractor,
) : ViewModel(),
    ISideEffectSource<CategoriesViewModel.SideEffect> by SideEffectQueue() {

    private val categoriesResult = MutableStateFlow<Result<List<Category>>?>(null)
    val categories = categoriesResult
        .mapState(viewModelScope) { it?.getOrNull().orEmpty().toPersistentList() }

    init {
        viewModelScope.launch {
            categoriesResult.value = interactor.getCategories()
        }
    }

    sealed interface SideEffect : ISideEffectSource.ISideEffect

}

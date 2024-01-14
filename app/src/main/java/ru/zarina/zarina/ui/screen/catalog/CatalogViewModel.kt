package ru.zarina.zarina.ui.screen.catalog

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.common.Category
import ru.zarina.zarina.domain.rework.common.findMenMainCategory
import ru.zarina.zarina.domain.rework.common.findWomenMainCategory
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.utils.clean.invoke
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: CatalogInteractor,
) : ViewModel(), SideEffectSource<CatalogViewModel.SideEffect> by SideEffectSourceImpl() {

    private var fetchCategoriesJob: Job? = null

    val searchQuery = savedStateHandle.getStateFlow(
        key = KEY_SEARCH_QUERY,
        initialValue = "",
    )

    val genderTabs = MutableStateFlow(GenderTab.entries.toList()).asStateFlow()

    val currentGenderTab = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_TAB,
        initialValue = GenderTab.WOMEN,
    )

    private val _categoryListState = MutableStateFlow<CategoryListState>(CategoryListState.Loading)
    val categoryListState = _categoryListState.asStateFlow()

    init {
        fetchCategories()
    }

    fun onSearchQueryChanged(query: String) {
        savedStateHandle[KEY_SEARCH_QUERY] = query
    }

    fun onSearchBarClearClicked() {
        savedStateHandle[KEY_SEARCH_QUERY] = ""
    }

    fun onSearchBarCancelClicked() {
        emitSideEffect(SideEffect.FreeSearchBarFocus)
    }

    fun onGenderTabClicked(tab: GenderTab) {
        savedStateHandle[KEY_CURRENT_GENDER_TAB] = tab
    }

    fun onCategoryClicked(category: Category) {
        // TODO: [High] Implement
    }

    private fun fetchCategories() {
        fetchCategoriesJob?.cancel()
        fetchCategoriesJob = viewModelScope.launch {
            interactor.getCategories().collect { result ->
                val categoryListState = result.fold(
                    onSuccess = { categories ->
                        val womenCategories =
                            categories.findWomenMainCategory()?.children ?: emptyList()
                        val menCategories =
                            categories.findMenMainCategory()?.children ?: emptyList()
                        CategoryListState.Success(womenCategories, menCategories)
                    },
                    onFailure = { throwable ->
                        val errorState = when (throwable) {
                            is IOException -> ErrorStateRework.NETWORK
                            else -> ErrorStateRework.GENERIC
                        }
                        CategoryListState.Error(errorState)
                    },
                )
                _categoryListState.value = categoryListState
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data object FreeSearchBarFocus : SideEffect
    }

    @Parcelize
    enum class GenderTab : Parcelable { WOMEN, MEN }

    @Stable
    sealed class CategoryListState {
        data object Loading : CategoryListState()

        @Immutable
        data class Success(
            val womenCategories: List<Category>,
            val menCategories: List<Category>,
        ) : CategoryListState()

        @Immutable
        data class Error(val state: ErrorStateRework) : CategoryListState()
    }

    @Immutable
    data class CategoryItem(
        val category: Category,
        val isVisible: Boolean,
        val nestingLevel: Int,
        val isExpandable: Boolean,
        val isExpanded: Boolean,
    )

    companion object {
        private const val KEY_SEARCH_QUERY = "search_query"
        private const val KEY_CURRENT_GENDER_TAB = "current_gender_tab"
    }
}

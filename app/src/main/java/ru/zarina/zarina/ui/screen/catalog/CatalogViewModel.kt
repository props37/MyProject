package ru.zarina.zarina.ui.screen.catalog

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableSet
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.plus
import kotlinx.parcelize.Parcelize
import ru.zarina.zarina.domain.rework.category.Categories
import ru.zarina.zarina.domain.rework.category.Category
import ru.zarina.zarina.domain.rework.category.withFlattenedChildren
import ru.zarina.zarina.ui.common.base.ErrorStateRework
import ru.zarina.zarina.ui.common.base.Throttler
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSource
import ru.zarina.zarina.ui.common.base.sideeffectsource.SideEffectSourceImpl
import ru.zarina.zarina.util.library.coroutines.WhileAndroidUiSubscribed
import ru.zarina.zarina.utils.clean.invoke
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: CatalogInteractor,
) : ViewModel(), SideEffectSource<CatalogViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val searchQuery: StateFlow<String> = savedStateHandle.getStateFlow(
        key = KEY_SEARCH_QUERY,
        initialValue = "",
    )

    val genderTabs: StateFlow<ImmutableList<GenderTab>> =
        MutableStateFlow(GenderTab.entries.toImmutableList()).asStateFlow()

    val currentGenderTab: StateFlow<GenderTab> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_TAB,
        initialValue = GenderTab.WOMEN,
    )

    private val categoriesFetchRequests = MutableSharedFlow<Unit>(replay = 1).also { it.tryEmit(Unit) }

    private val isFetchingCategories = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val categoriesResult: StateFlow<Result<Categories>?> = categoriesFetchRequests
        .flatMapLatest {
            interactor.getCategoriesFlow()
        }
        .onEach { isFetchingCategories.value = false }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    private val expandedCategories = MutableStateFlow<Set<Category>>(emptySet())

    val categoryListState: StateFlow<CategoryListState> = combine(
        isFetchingCategories,
        categoriesResult,
    ) { isFetchingCategories, categoriesResult ->
        if (isFetchingCategories || categoriesResult == null) {
            CategoryListState.Loading
        } else {
            categoriesResult.fold(
                onSuccess = { categories ->
                    val womenCategoryItems = categories.women
                        .flatMapToCategoryItems(CategoryListItem.NESTING_LEVEL_MIN_VALUE)
                        .toImmutableList()
                    val menCategoryItems = categories.men
                        .flatMapToCategoryItems(CategoryListItem.NESTING_LEVEL_MIN_VALUE)
                        .toImmutableList()
                    CategoryListState.Success(womenCategoryItems, menCategoryItems)
                },
                onFailure = { throwable ->
                    val state = when (throwable) {
                        is IOException -> ErrorStateRework.NETWORK
                        else -> ErrorStateRework.GENERIC
                    }
                    CategoryListState.Error(state)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope + Dispatchers.Default,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CategoryListState.Loading,
    )

    val categoryListItemsState: StateFlow<CategoryListItemsState> = combine(
        categoriesResult,
        expandedCategories,
    ) { categoriesResult, expandedCategories ->
        val categories = categoriesResult?.getOrNull()
        val expandedCategoryChildIds = expandedCategories.flatMap { category ->
            category.children?.map { it.id } ?: emptyList()
        }
        val visibleCategoryIds = if (categories != null) {
            val topMostCategoryIds = (categories.women + categories.men).map { it.id }
            topMostCategoryIds + expandedCategoryChildIds
        } else {
            expandedCategoryChildIds
        }

        CategoryListItemsState(
            visibleCategoryIds = visibleCategoryIds.toImmutableSet(),
            expandedCategoryIds = expandedCategories.map { it.id }.toImmutableSet(),
        )
    }.stateIn(
        scope = viewModelScope + Dispatchers.Default,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CategoryListItemsState(persistentSetOf(), persistentSetOf()),
    )

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

    fun onCategoryListItemClicked(item: CategoryListItem) {
        when (item) {
            is CategoryListItem.CategoryItem -> onCategoryItemClicked(item)
            is CategoryListItem.SeeWholeCategoryItem -> onSeeWholeCategoryItemClicked(item)
        }
    }

    fun onCategoryListErrorRefreshClicked() {
        isFetchingCategories.value = true
        categoriesFetchRequests.tryEmit(Unit)
    }

    private fun onCategoryItemClicked(item: CategoryListItem.CategoryItem) {
        val category = item.category
        if (category.children.isNullOrEmpty()) {
            navigationThrottler.throttle {
                val action = CatalogScreenAction.CategoryClicked(item.category)
                emitSideEffect(SideEffect.NavigateForward(action))
            }
        } else {
            expandedCategories.update { set ->
                val ids = set.mapTo(mutableSetOf()) { it.id }
                if (category.id !in ids) {
                    set + category
                } else {
                    val impactedCategories = category.withFlattenedChildren()
                    set - impactedCategories.toSet()
                }
            }
        }
    }

    private fun onSeeWholeCategoryItemClicked(item: CategoryListItem.SeeWholeCategoryItem) {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.CategoryClicked(item.category)
            emitSideEffect(SideEffect.NavigateForward(action))
        }
    }

    private fun List<Category>.flatMapToCategoryItems(initialNestingLevel: Int): List<CategoryListItem> {
        return this.flatMap { category ->
            category.flatMapToCategoryItems(initialNestingLevel)
        }
    }

    private fun Category.flatMapToCategoryItems(initialNestingLevel: Int): List<CategoryListItem> {
        val category = this
        return buildList {
            val item = CategoryListItem.fromCategory(category, initialNestingLevel)
            add(item)

            val childItemsNestingLevel = initialNestingLevel + 1
            val childItems = category.children?.flatMap { category ->
                category.flatMapToCategoryItems(childItemsNestingLevel)
            }
            if (childItems != null) {
                val seeWholeCategoryItem = CategoryListItem.SeeWholeCategoryItem(
                    category = category,
                    nestingLevel = childItemsNestingLevel,
                )
                add(seeWholeCategoryItem)
                addAll(childItems)
            }
        }
    }

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class NavigateForward(val action: CatalogScreenAction) : SideEffect
        data object FreeSearchBarFocus : SideEffect
    }

    @Parcelize
    enum class GenderTab : Parcelable { WOMEN, MEN }

    @Stable
    sealed class CategoryListState {
        data object Loading : CategoryListState()

        @Immutable
        data class Success(
            val womenItems: ImmutableList<CategoryListItem>,
            val menItems: ImmutableList<CategoryListItem>,
        ) : CategoryListState()

        @Immutable
        data class Error(val state: ErrorStateRework) : CategoryListState()
    }

    @Immutable
    data class CategoryListItemsState(
        val visibleCategoryIds: ImmutableSet<Category.Id>,
        val expandedCategoryIds: ImmutableSet<Category.Id>,
    )

    @Stable
    sealed class CategoryListItem(
        open val id: Id,
        open val nestingLevel: Int,
    ) {
        @Immutable
        data class CategoryItem(
            val category: Category,
            override val nestingLevel: Int,
            val isExpandable: Boolean,
        ) : CategoryListItem(
            id = createId(category),
            nestingLevel = nestingLevel,
        ) {
            init {
                checkNestingLevel()
            }

            companion object {
                private fun createId(category: Category): Id {
                    return Id(category.id.value.toString())
                }
            }
        }

        @Immutable
        data class SeeWholeCategoryItem(
            val category: Category,
            override val nestingLevel: Int,
        ) : CategoryListItem(
            id = createId(category),
            nestingLevel = nestingLevel,
        ) {
            init {
                checkNestingLevel()
            }

            companion object {
                private const val ID_PREFIX = "see_whole_category"

                private fun createId(category: Category): Id {
                    return Id("$ID_PREFIX${category.id.value}")
                }
            }
        }

        @JvmInline
        value class Id(val value: String)

        protected fun checkNestingLevel() {
            check(nestingLevel >= NESTING_LEVEL_MIN_VALUE) {
                "nestingLevel $nestingLevel must be at least $NESTING_LEVEL_MIN_VALUE"
            }
        }

        companion object {
            const val NESTING_LEVEL_MIN_VALUE = 0

            fun fromCategory(category: Category, nestingLevel: Int): CategoryItem {
                return CategoryItem(
                    category = category,
                    nestingLevel = nestingLevel,
                    isExpandable = !category.children.isNullOrEmpty(),
                )
            }
        }
    }

    companion object {
        private const val KEY_SEARCH_QUERY = "search_query"
        private const val KEY_CURRENT_GENDER_TAB = "current_gender_tab"
    }
}

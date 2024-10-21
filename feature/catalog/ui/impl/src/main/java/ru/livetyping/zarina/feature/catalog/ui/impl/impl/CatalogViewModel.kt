package ru.livetyping.zarina.feature.catalog.ui.impl.impl

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.persistentSetOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import ru.livetyping.zarina.core.coroutinesutil.FlowRequest
import ru.livetyping.zarina.core.coroutinesutil.FlowRequester
import ru.livetyping.zarina.core.coroutinesutil.WhileAndroidUiSubscribed
import ru.livetyping.zarina.core.coroutinesutil.mapState
import ru.livetyping.zarina.core.domain.model.category.Categories
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.category.withFlattenedChildren
import ru.livetyping.zarina.core.domain.model.gender.Gender
import ru.livetyping.zarina.core.domain.usecase.category.GetCategoriesFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.GetLastContentGenderFlowUseCase
import ru.livetyping.zarina.core.domain.usecase.gender.SetLastContentGenderUseCase
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSource
import ru.livetyping.zarina.core.uicommon.sideeffect.SideEffectSourceImpl
import ru.livetyping.zarina.core.uicommon.throttler.Throttler
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreenState
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getLastContentGenderFlow: GetLastContentGenderFlowUseCase,
    private val getCategoriesFlow: GetCategoriesFlowUseCase,
    private val setLastContentGender: SetLastContentGenderUseCase,
) : ViewModel(), SideEffectSource<CatalogSideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    private val currentGender = MutableStateFlow(getCurrentGenderInitialValue())

    val genderSelectorState: StateFlow<TabRowState<GenderTab>> = currentGender.mapState(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
    ) { currentGender ->
        TabRowState(
            tabs = GenderTab.getTabs().toImmutableList(),
            currentTab = currentGender,
        )
    }

    private val categoriesRequester = FlowRequester(CategoriesRequest) {
        getCategoriesFlow()
    }

    private val categoriesResult: StateFlow<Result<Categories>?> = categoriesRequester.flow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = null,
        )

    val categoryListState: StateFlow<CategoryListState> = combine(
        categoriesRequester.loadingState,
        categoriesResult,
    ) { loadingState, result ->
        createCategoryListState(loadingState, result)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CategoryListState.Loading,
    )

    private val expandedCategories = MutableStateFlow<Set<Category>>(emptySet())

    val categoryListItemsState: StateFlow<CategoryListItemsState> = combine(
        categoriesResult,
        expandedCategories,
    ) { categoriesResult, expandedCategories ->
        createCategoryListItemsState(
            categoriesResult = categoriesResult,
            expandedCategories = expandedCategories,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileAndroidUiSubscribed,
        initialValue = CategoryListItemsState(
            visibleCategoryIds = persistentSetOf(),
            expandedCategoryIds = persistentSetOf(),
        ),
    )

    fun onSearchBarClicked() {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.SearchClicked
            emitSideEffect(CatalogSideEffect.Navigate(action))
        }
    }

    fun onGenderSelectorEvent(event: TabRowEvent<GenderTab>) {
        when (event) {
            is TabRowEvent.TabChanged -> {
                val genderTab = event.tab
                currentGender.value = genderTab
                viewModelScope.launch {
                    val params = SetLastContentGenderUseCase.Params(genderTab.toGender())
                    setLastContentGender(params)
                }
            }

            // TODO: [Low] Implement
            is TabRowEvent.TabReselected -> TODO()
        }
    }

    fun onCategoryListEvent(event: CategoryListEvent) {
        when (event) {
            is CategoryListEvent.ItemClicked -> onCategoryListItemClicked(event.item)
            CategoryListEvent.ErrorRefreshClicked -> {
                categoriesRequester.request(CategoriesRequest)
            }
        }
    }

    private fun onCategoryListItemClicked(item: CategoryListItem) {
        when (item) {
            is CategoryListItem.CategoryItem -> onCategoryItemClicked(item)
            is CategoryListItem.SeeWholeCategoryItem -> onSeeWholeCategoryItemClicked(item)
        }
    }

    private fun onCategoryItemClicked(item: CategoryListItem.CategoryItem) {
        val category = item.category
        if (!category.isExpandable || category.children.isNullOrEmpty()) {
            navigationThrottler.throttle {
                val action = CatalogScreenAction.CategoryClicked(item.category.id)
                emitSideEffect(CatalogSideEffect.Navigate(action))
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
            val action = CatalogScreenAction.CategoryClicked(item.category.id)
            emitSideEffect(CatalogSideEffect.Navigate(action))
        }
    }

    private fun getCurrentGenderInitialValue(): GenderTab {
        return runBlocking {
            val genderResult = getLastContentGenderFlow().firstOrNull()
            val gender = genderResult?.getOrNull() ?: Gender.getDefault()
            GenderTab.from(gender)
        }
    }

    private fun createCategoryListState(
        categoryLoadingState: FlowRequester.LoadingState,
        categoryResult: Result<Categories>?,
    ): CategoryListState {
        return if (categoryLoadingState.isLoading() || categoryResult == null) {
            CategoryListState.Loading
        } else {
            categoryResult.fold(
                onSuccess = { categories ->
                    val womenCategoryItems = categories.women
                        .flatMapToCategoryItems(CategoryListItem.NESTING_LEVEL_MIN_VALUE)
                        .toImmutableList()
                    val menCategoryItems = categories.men
                        .flatMapToCategoryItems(CategoryListItem.NESTING_LEVEL_MIN_VALUE)
                        .toImmutableList()
                    CategoryListState.Success(womenCategoryItems, menCategoryItems)
                },
                onFailure = { t ->
                    val errorState = ZarinaErrorScreenState.from(t)
                    CategoryListState.Error(errorState)
                },
            )
        }
    }

    private fun createCategoryListItemsState(
        categoriesResult: Result<Categories>?,
        expandedCategories: Set<Category>,
    ): CategoryListItemsState {
        val categories = categoriesResult?.getOrNull()
        val expandedCategoryChildrenIds = expandedCategories.flatMap { category ->
            category.children?.map { it.id } ?: emptyList()
        }
        val visibleCategoryIds = if (categories != null) {
            val topMostCategoryIds = (categories.women + categories.men).map { it.id }
            topMostCategoryIds + expandedCategoryChildrenIds
        } else {
            expandedCategoryChildrenIds
        }
        val expandedCategoryIds = expandedCategories.map { it.id }

        return CategoryListItemsState(
            visibleCategoryIds = visibleCategoryIds.toImmutableSet(),
            expandedCategoryIds = expandedCategoryIds.toImmutableSet(),
        )
    }


    private fun List<Category>.flatMapToCategoryItems(
        initialNestingLevel: Int,
    ): List<CategoryListItem> {
        return this.flatMap { category ->
            category.flatMapToCategoryItems(initialNestingLevel)
        }
    }

    private fun Category.flatMapToCategoryItems(initialNestingLevel: Int): List<CategoryListItem> {
        val category = this
        return buildList {
            val item = CategoryListItem.fromCategory(category, initialNestingLevel)
            add(item)

            val children = category.children
            if (category.isExpandable && !children.isNullOrEmpty()) {
                val childItemsNestingLevel = initialNestingLevel + 1
                val childItems = children.flatMap { category ->
                    category.flatMapToCategoryItems(childItemsNestingLevel)
                }
                val seeWholeCategoryItem = CategoryListItem.SeeWholeCategoryItem(
                    category = category,
                    nestingLevel = childItemsNestingLevel,
                )
                add(seeWholeCategoryItem)
                addAll(childItems)
            }
        }
    }

    private data object CategoriesRequest : FlowRequest
}

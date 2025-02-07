package ru.livetyping.zarina.presentation.screen.catalog

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.runBlocking
import kotlinx.parcelize.Parcelize
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSource
import ru.livetyping.zarina.base.sideeffectsource.SideEffectSourceImpl
import ru.livetyping.zarina.base.throttler.Throttler
import ru.livetyping.zarina.data.analytics.AppMetricaHelper
import ru.livetyping.zarina.data.analytics.AppMetricaScreen
import ru.livetyping.zarina.domain.category.Categories
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.domain.category.withFlattenedChildren
import ru.livetyping.zarina.domain.common.Gender
import ru.livetyping.zarina.presentation.common.error.ErrorState
import ru.livetyping.zarina.presentation.common.error.from
import ru.livetyping.zarina.presentation.common.util.getNavigationThrottler
import ru.livetyping.zarina.usecase.user.SetUserContentGenderUseCase
import ru.livetyping.zarina.util.base.usecase.invoke
import ru.livetyping.zarina.util.library.coroutines.FlowRequester
import ru.livetyping.zarina.util.library.coroutines.ImmutableStateFlow
import ru.livetyping.zarina.util.library.coroutines.WhileUiSubscribed
import javax.inject.Inject

@HiltViewModel
class CatalogViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val interactor: CatalogInteractor,
) : ViewModel(), SideEffectSource<CatalogViewModel.SideEffect> by SideEffectSourceImpl() {

    private val navigationThrottler = Throttler.getNavigationThrottler()

    val genderTabs: StateFlow<ImmutableList<GenderTab>> =
        ImmutableStateFlow(GenderTab.entries.toImmutableList())

    val currentGenderTab: StateFlow<GenderTab> = savedStateHandle.getStateFlow(
        key = KEY_CURRENT_GENDER_TAB,
        initialValue = runBlocking {
            val gender = interactor.getUserContentGenderFlow()
                .firstOrNull()
                ?.getOrNull()
                ?: Gender.getDefault()
            GenderTab.from(gender)
        },
    )

    private val categoriesRequester = FlowRequester(CategoriesRequest) {
        interactor.getCategoriesFlow()
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
    ) { categoriesLoadingState, categoriesResult ->
        if (categoriesLoadingState.isLoading() || categoriesResult == null) {
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
                    val state = ErrorState.from(throwable)
                    CategoryListState.Error(state)
                },
            )
        }
    }.stateIn(
        scope = viewModelScope + Dispatchers.Default,
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CategoryListState.Loading,
    )

    private val expandedCategories = MutableStateFlow<Set<Category>>(emptySet())

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
        started = SharingStarted.WhileUiSubscribed,
        initialValue = CategoryListItemsState(persistentSetOf(), persistentSetOf()),
    )

    fun onScreenCreated() {
        AppMetricaHelper.reportScreenOpened(AppMetricaScreen.Catalog)
    }

    fun onSearchBarClicked() {
        navigationThrottler.throttle {
            val action = CatalogScreenAction.SearchClicked
            emitSideEffect(SideEffect.Navigate(action))
        }
    }

    fun onGenderTabChanged(tab: GenderTab) {
        savedStateHandle[KEY_CURRENT_GENDER_TAB] = tab
        viewModelScope.launch {
            val params = SetUserContentGenderUseCase.Params(tab.toGender())
            interactor.setUserContentGender(params)
        }
    }

    fun onCategoryListItemClicked(item: CategoryListItem) {
        when (item) {
            is CategoryListItem.CategoryItem -> onCategoryItemClicked(item)
            is CategoryListItem.SeeWholeCategoryItem -> onSeeWholeCategoryItemClicked(item)
        }
    }

    fun onCategoryListErrorRefreshClicked() {
        categoriesRequester.request(CategoriesRequest)
    }

    private fun onCategoryItemClicked(item: CategoryListItem.CategoryItem) {
        val category = item.category
        if (!category.isExpandable || category.children.isNullOrEmpty()) {
            navigationThrottler.throttle {
                val action = CatalogScreenAction.CategoryClicked(item.category.id)
                emitSideEffect(SideEffect.Navigate(action))
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
            emitSideEffect(SideEffect.Navigate(action))
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

            if (category.isExpandable && !category.children.isNullOrEmpty()) {
                val childItemsNestingLevel = initialNestingLevel + 1
                val childItems = category.children.flatMap { category ->
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

    sealed interface SideEffect : SideEffectSource.SideEffect {
        data class Navigate(val action: CatalogScreenAction) : SideEffect
    }

    @Parcelize
    enum class GenderTab : Parcelable {
        WOMEN,
        MEN;

        fun toGender(): Gender = when (this) {
            WOMEN -> Gender.FEMALE
            MEN -> Gender.MALE
        }

        companion object {
            fun from(gender: Gender): GenderTab = when (gender) {
                Gender.FEMALE -> WOMEN
                Gender.MALE -> MEN
            }
        }
    }

    @Stable
    sealed class CategoryListState {
        data object Loading : CategoryListState()

        @Immutable
        data class Success(
            val womenItems: ImmutableList<CategoryListItem>,
            val menItems: ImmutableList<CategoryListItem>,
        ) : CategoryListState()

        @Immutable
        data class Error(val state: ErrorState) : CategoryListState()
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
                    isExpandable = category.isExpandable && !category.children.isNullOrEmpty(),
                )
            }
        }
    }

    private data object CategoriesRequest : FlowRequester.Request

    companion object {
        private const val KEY_CURRENT_GENDER_TAB = "current_gender_tab"
    }
}

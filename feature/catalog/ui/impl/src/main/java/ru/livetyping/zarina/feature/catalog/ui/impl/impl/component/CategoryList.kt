package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.category.CategoryListEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.category.CategoryListItem
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.category.CategoryListItemsState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.category.CategoryListState

@Composable
internal fun CategoryList(
    gender: GenderTab,
    categoryListState: CategoryListState,
    onCategoryListEvent: (CategoryListEvent) -> Unit,
    categoryListItemsState: CategoryListItemsState,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = categoryListState,
        contentKey = { state ->
            when (state) {
                is CategoryListState.Success -> CategoryListKey.Success
                is CategoryListState.Error -> state
                CategoryListState.Loading -> state
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is CategoryListState.Success -> {
                val items = when (gender) {
                    GenderTab.WOMEN -> state.womenItems
                    GenderTab.MEN -> state.menItems
                }

                CategoryListSuccess(
                    categoryListItems = items,
                    onCategoryListEvent = onCategoryListEvent,
                    categoryListItemsState = categoryListItemsState,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            CategoryListState.Loading -> {
                CategoryListSkeleton(modifier = Modifier.fillMaxSize())
            }

            is CategoryListState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = {
                        onCategoryListEvent(CategoryListEvent.ErrorRefreshClicked)
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun CategoryListSuccess(
    categoryListItems: ImmutableList<CategoryListItem>,
    onCategoryListEvent: (CategoryListEvent) -> Unit,
    categoryListItemsState: CategoryListItemsState,
    modifier: Modifier = Modifier,
) {
    val lastVisibleItemIndex = remember(categoryListItems, categoryListItemsState) {
        categoryListItems.indexOfLast { it.isVisible(categoryListItemsState) }
    }

    LazyColumn(
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        for (index in categoryListItems.indices) {
            val item = categoryListItems[index]
            if (item.isVisible(categoryListItemsState)) {
                item(
                    key = item.id.value,
                    contentType = getCategoryListItemContentType(item),
                ) {
                    Column(modifier = Modifier.animateZarinaItem(lazyItemScope = this)) {
                        when (item) {
                            is CategoryListItem.CategoryItem -> {
                                val isExpanded =
                                    item.category.id in categoryListItemsState.expandedCategoryIds

                                CategoryItem(
                                    item = item,
                                    onItemClicked = {
                                        onCategoryListEvent(CategoryListEvent.ItemClicked(it))
                                    },
                                    isExpanded = isExpanded,
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }

                            is CategoryListItem.SeeWholeCategoryItem -> {
                                SeeWholeCategoryItem(
                                    item = item,
                                    onItemClicked = {
                                        onCategoryListEvent(CategoryListEvent.ItemClicked(it))
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                )
                            }
                        }

                        if (index < lastVisibleItemIndex) {
                            ZarinaDivider(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryListSkeleton(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

    Column(modifier = modifier) {
        repeat(CategoryListSkeletonItemCount) { index ->
            CategoryItemSkeleton(
                index = index,
                shimmer = shimmer,
                modifier = Modifier.fillMaxWidth(),
            )

            if (index != CategoryListSkeletonItemCount - 1) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }

        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
    }
}

private fun CategoryListItem.isVisible(itemsState: CategoryListItemsState): Boolean {
    return when (this) {
        is CategoryListItem.CategoryItem -> {
            this.category.id in itemsState.visibleCategoryIds
        }

        is CategoryListItem.SeeWholeCategoryItem -> {
            this.category.id in itemsState.expandedCategoryIds
        }
    }
}

@Stable
private fun getCategoryListItemContentType(item: CategoryListItem): CategoryListItemContentType {
    return when (item) {
        is CategoryListItem.CategoryItem -> CategoryListItemContentType.CategoryItem
        is CategoryListItem.SeeWholeCategoryItem -> CategoryListItemContentType.SeeWholeCategoryItem
    }
}

private const val CategoryListSkeletonItemCount = 16

private enum class CategoryListKey { Success }

private enum class CategoryListItemContentType { CategoryItem, SeeWholeCategoryItem }

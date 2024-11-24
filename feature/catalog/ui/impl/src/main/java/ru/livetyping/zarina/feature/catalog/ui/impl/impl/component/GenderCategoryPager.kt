package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListItemsState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListState

@Composable
internal fun GenderCategoryPager(
    genderSelectorState: TabRowState<GenderTab>,
    categoryListState: CategoryListState,
    onCategoryListEvent: (CategoryListEvent) -> Unit,
    categoryListItemsState: CategoryListItemsState,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
) {
    HorizontalPager(
        state = pagerState,
        modifier = modifier,
    ) { page ->
        val gender = genderSelectorState.tabs[page]

        CategoryList(
            gender = gender,
            categoryListState = categoryListState,
            onCategoryListEvent = onCategoryListEvent,
            categoryListItemsState = categoryListItemsState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

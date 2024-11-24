package ru.livetyping.zarina.feature.catalog.ui.impl.impl.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.pager.rememberPagerConnectedToTabRowState
import ru.livetyping.zarina.core.uimodel.tab.GenderTab
import ru.livetyping.zarina.core.uimodel.tab.TabRowEvent
import ru.livetyping.zarina.core.uimodel.tab.TabRowState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListEvent
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListItemsState
import ru.livetyping.zarina.feature.catalog.ui.impl.impl.model.CategoryListState

@Composable
internal fun CatalogContent(
    genderSelectorState: TabRowState<GenderTab>,
    onGenderSelectorEvent: (TabRowEvent<GenderTab>) -> Unit,
    categoryListState: CategoryListState,
    onCategoryListEvent: (CategoryListEvent) -> Unit,
    categoryListItemsState: CategoryListItemsState,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        val genderSelectorPagerState = rememberPagerConnectedToTabRowState(
            tabs = genderSelectorState.tabs,
            currentTab = genderSelectorState.currentTab,
            onTabChanged = { onGenderSelectorEvent(TabRowEvent.TabChanged(it)) },
            initialPage = remember { genderSelectorState.currentTabIndex },
            pageCount = { genderSelectorState.tabs.size },
        )

        GenderSelector(
            genderSelectorState = genderSelectorState,
            onGenderSelectorEvent = onGenderSelectorEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
        )

        GenderCategoryPager(
            genderSelectorState = genderSelectorState,
            categoryListState = categoryListState,
            onCategoryListEvent = onCategoryListEvent,
            categoryListItemsState = categoryListItemsState,
            pagerState = genderSelectorPagerState,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
private fun GenderCategoryPager(
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

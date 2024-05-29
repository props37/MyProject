package ru.livetyping.zarina.presentation.screen.catalog

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.R
import ru.livetyping.zarina.presentation.common.component.divider.ZarinaDivider
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTab
import ru.livetyping.zarina.presentation.common.component.tab.ZarinaTabRow
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextField
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldDefaults
import ru.livetyping.zarina.presentation.common.component.textfield.ZarinaTextFieldSize
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.util.domain.toComposeColor
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.CategoryListItem
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.CategoryListItemsState
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.CategoryListState
import ru.livetyping.zarina.presentation.screen.catalog.CatalogViewModel.GenderTab
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.Crossfade

@Suppress("ConstPropertyName")
object CatalogScreenComponents {

    @Composable
    fun SearchBar(
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .heightIn(min = TopBarDefaults.MinHeight)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp),
        ) {
            ZarinaTextField(
                value = "",
                onValueChanged = {},
                isEnabled = false,
                size = ZarinaTextFieldSize.Small,
                placeholder = { Text(text = stringResource(R.string.find_products)) },
                leadingContent = {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.ic_magnifying_glass_24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                colors = ZarinaTextFieldDefaults.colors(
                    disabledPlaceholderColor = UiKitTheme.colors.text.general.regular.muted,
                    disabledIndicationLineColor = UiKitTheme.colors.border.general.default,
                )
            )
        }
    }

    @Composable
    fun GenderPicker(
        genders: ImmutableList<GenderTab>,
        pagerState: PagerState,
        onGenderChanged: (GenderTab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val currentGender = remember(genders, pagerState) {
            derivedStateOf { genders.getOrNull(pagerState.currentPage) }
        }

        ZarinaTabRow(
            selectedTabIndex = pagerState.currentPage,
            modifier = modifier,
        ) {
            genders.forEach { gender ->
                val textResId = when (gender) {
                    GenderTab.WOMEN -> R.string.for_women
                    GenderTab.MEN -> R.string.for_men
                }
                ZarinaTab(
                    text = stringResource(textResId).uppercase(),
                    onClick = { onGenderChanged(gender) },
                    isSelected = gender == currentGender.value,
                    selectedTextStyle = UiKitTheme.typography.tertiary.regular,
                    unselectedTextStyle = UiKitTheme.typography.tertiary.light,
                )
            }
        }
    }

    @Composable
    fun GenderCategoryPager(
        genders: ImmutableList<GenderTab>,
        pagerState: PagerState,
        categoryListState: CategoryListState,
        categoryListItemsState: CategoryListItemsState,
        onCategoryListItemClicked: (CategoryListItem) -> Unit,
        onCategoryListErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier,
        ) { page ->
            CategoryList(
                gender = genders[page],
                state = categoryListState,
                itemsState = categoryListItemsState,
                onItemClicked = onCategoryListItemClicked,
                onErrorRefreshClicked = onCategoryListErrorRefreshClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @Composable
    private fun CategoryList(
        gender: GenderTab,
        state: CategoryListState,
        itemsState: CategoryListItemsState,
        onItemClicked: (CategoryListItem) -> Unit,
        onErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        Crossfade(
            targetState = state,
            contentKey = { getCategoryListContentKey(it) },
            modifier = modifier,
        ) { state ->
            when (state) {
                is CategoryListState.Success -> {
                    val items = when (gender) {
                        GenderTab.WOMEN -> state.womenItems
                        GenderTab.MEN -> state.menItems
                    }

                    CategoryItems(
                        items = items,
                        itemsState = itemsState,
                        onItemClicked = onItemClicked,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                CategoryListState.Loading -> {
                    CategoryListSkeleton(modifier = Modifier.fillMaxSize())
                }

                is CategoryListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.state,
                        onButtonClicked = onErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun CategoryItems(
        items: ImmutableList<CategoryListItem>,
        itemsState: CategoryListItemsState,
        onItemClicked: (CategoryListItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val lastVisibleItemIndex = remember(items, itemsState) {
            items.indexOfLast { it.isVisible(itemsState) }
        }

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier,
        ) {
            items.forEachIndexed { index, item ->
                if (item.isVisible(itemsState)) {
                    item(
                        key = item.id.value,
                        contentType = getCategoryListItemContentType(item),
                    ) {
                        Column(modifier = Modifier.animateItem()) {
                            when (item) {
                                is CategoryListItem.CategoryItem -> {
                                    val isExpanded =
                                        item.category.id in itemsState.expandedCategoryIds

                                    CategoryItem(
                                        item = item,
                                        onItemClicked = onItemClicked,
                                        isExpanded = isExpanded,
                                        modifier = Modifier.fillMaxWidth(),
                                    )
                                }

                                is CategoryListItem.SeeWholeCategoryItem -> {
                                    SeeWholeCategoryItem(
                                        item = item,
                                        onItemClicked = onItemClicked,
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
    private fun CategoryItem(
        item: CategoryListItem.CategoryItem,
        onItemClicked: (CategoryListItem.CategoryItem) -> Unit,
        isExpanded: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = CategoryListItemMinHeight)
                .clickable { onItemClicked(item) }
                .padding(CategoryListItemContentPadding)
                .padding(start = CategoryListItemNestingStartPadding * item.nestingLevel),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                val color = item.category.color?.toComposeColor()
                    ?: UiKitTheme.colors.text.general.regular.default

                Text(
                    text = item.category.name.uppercase(),
                    style = UiKitTheme.typography.tertiary.light,
                    color = color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                if (item.category.label != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = item.category.label.uppercase(),
                        style = UiKitTheme.typography.caption2.light,
                        color = color,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.align(Alignment.Top),
                    )
                }
            }

            if (item.isExpandable) {
                Spacer(modifier = Modifier.width(8.dp))

                val rotation = animateFloatAsState(
                    targetValue = if (isExpanded) 0f else 180f,
                    animationSpec = tween(durationMillis = 200),
                    label = "CategoryItem Expand icon rotation",
                )
                val contentDescriptionResId =
                    if (isExpanded) R.string.collapse else R.string.expand

                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = stringResource(contentDescriptionResId),
                    modifier = Modifier
                        .size(16.dp)
                        .graphicsLayer {
                            rotationZ = rotation.value
                        },
                )
            }
        }
    }

    @Composable
    private fun SeeWholeCategoryItem(
        item: CategoryListItem.SeeWholeCategoryItem,
        onItemClicked: (CategoryListItem.SeeWholeCategoryItem) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = modifier
                .heightIn(min = CategoryListItemMinHeight)
                .clickable { onItemClicked(item) }
                .padding(CategoryListItemContentPadding)
                .padding(start = CategoryListItemNestingStartPadding * item.nestingLevel),
        ) {
            Text(
                text = stringResource(R.string.see_all).uppercase(),
                style = UiKitTheme.typography.tertiary.light,
                color = UiKitTheme.colors.text.general.regular.default,
                maxLines = 1,
            )
        }
    }

    @Composable
    private fun CategoryListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

        LazyColumn(
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = modifier,
        ) {
            items(
                count = CategoryListSkeletonItemCount,
                key = { it },
            ) { index ->
                Column(modifier = Modifier.animateItem()) {
                    CategoryListSkeletonItem(
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
            }
        }
    }

    @Composable
    private fun CategoryListSkeletonItem(
        index: Int,
        shimmer: Shimmer,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .heightIn(min = CategoryListItemMinHeight)
                .padding(horizontal = 16.dp),
        ) {
            @Suppress("MagicNumber")
            val widthFraction = when (index % 4) {
                0 -> 0.6f
                1 -> 0.72f
                2 -> 0.48f
                3 -> 0.4f
                else -> 0.4f
            }

            ZarinaTextSkeleton(
                textStyle = UiKitTheme.typography.tertiary.light,
                shimmer = shimmer,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(widthFraction),
            )

            ZarinaSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(16.dp),
            )
        }
    }

    private fun getCategoryListContentKey(state: CategoryListState): Any {
        return when (state) {
            is CategoryListState.Success -> CategoryListContentKeySuccess
            CategoryListState.Loading, is CategoryListState.Error -> state
        }
    }

    @Stable
    private fun getCategoryListItemContentType(item: CategoryListItem): String {
        return when (item) {
            is CategoryListItem.CategoryItem -> CategoryListItemContentTypeCategoryItem
            is CategoryListItem.SeeWholeCategoryItem ->
                CategoryListItemContentTypeSeeWholeCategoryItem
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

    private val CategoryListItemMinHeight: Dp get() = 56.dp
    private val CategoryListItemNestingStartPadding: Dp get() = 20.dp
    private val CategoryListItemContentPadding: PaddingValues
        get() = PaddingValues(horizontal = 16.dp, vertical = 8.dp)

    private const val CategoryListContentKeySuccess = "CategoryListContentKeySuccess"

    private const val CategoryListItemContentTypeCategoryItem =
        "CategoryListItemContentTypeCategoryItem"
    private const val CategoryListItemContentTypeSeeWholeCategoryItem =
        "CategoryListItemContentTypeSeeWholeCategoryItem"

    private const val CategoryListSkeletonItemCount = 20
}

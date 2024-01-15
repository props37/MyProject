package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.TabRow
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.shimmer
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.rework.common.Category
import ru.zarina.zarina.ui.common.component.TopBarDefaults
import ru.zarina.zarina.ui.common.component.ZarinaTabIndicator
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.common.util.domain.toComposeColor
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.CategoryItem
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.CategoryListItemsState
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.CategoryListState
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.GenderTab
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.Crossfade

object CatalogScreenComponents {

    @Composable
    fun SearchBar(
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        onClearClicked: () -> Unit,
        onCancelClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier.heightIn(min = TopBarDefaults.MinHeight),
        ) {
            val focusState = remember { mutableStateOf<FocusState?>(null) }

            ZarinaTextField(
                value = searchQuery,
                onValueChanged = onSearchQueryChanged,
                placeholder = {
                    Text(text = stringResource(R.string.find_products))
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_24),
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                },
                innerTrailingContent = {
                    AnimatedVisibility(
                        visible = searchQuery.isNotEmpty(),
                        enter = remember { AnimatedContentDefaultEnterTransition },
                        exit = remember { AnimatedContentDefaultExitTransition },
                    ) {
                        ZarinaTextFieldDefaults.ClearButton(onClick = onClearClicked)
                    }
                },
                outerTrailingContent = {
                    val isCancelButtonVisible = focusState.value?.isFocused == true
                    AnimatedContent(
                        targetState = isCancelButtonVisible,
                        transitionSpec = {
                            AnimatedContentDefaultTransitionSpec().using(SizeTransform(clip = false))
                        },
                        contentAlignment = Alignment.Center,
                        label = "SearchBar Cancel button",
                    ) { isVisible ->
                        if (isVisible) {
                            ZarinaTextFieldDefaults.CancelButton(onClick = onCancelClicked)
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState.value = it },
            )
        }
    }

    @Composable
    fun GenderPicker(
        genders: List<GenderTab>,
        currentGender: GenderTab,
        onGenderClicked: (GenderTab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val selectedTabIndex = remember(genders, currentGender) {
            genders.indexOf(currentGender)
        }

        // TODO: [Medium] Extract?
        TabRow(
            selectedTabIndex = selectedTabIndex,
            backgroundColor = Color.Unspecified,
            indicator = { tabPositions ->
                ZarinaTabIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                )
            },
            divider = {},
            modifier = modifier,
        ) {
            genders.forEach { gender ->
                ZarinaButton(
                    onClick = { onGenderClicked(gender) },
                    size = ZarinaButtonSize.Medium,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
                ) {
                    val textResId = when (gender) {
                        GenderTab.WOMEN -> R.string.for_women
                        GenderTab.MEN -> R.string.for_men
                    }

                    val style = if (gender == currentGender) {
                        UiKitTheme.typographyReworked.tertiary.regular
                    } else {
                        UiKitTheme.typographyReworked.tertiary.light
                    }

                    Text(
                        text = stringResource(textResId).uppercase(),
                        style = style,
                        color = UiKitTheme.colorsReworked.text.general.regular.default,
                    )
                }
            }
        }
    }

    @OptIn(ExperimentalFoundationApi::class)
    @Composable
    fun GenderCategoryPager(
        genders: List<GenderTab>,
        currentGender: GenderTab,
        categoryListState: CategoryListState,
        categoryListItemsState: CategoryListItemsState,
        onCategoryClicked: (Category) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberPagerState(
            initialPage = remember { genders.indexOf(currentGender) },
            pageCount = { genders.size },
        )

        LaunchedEffect(pagerState, genders, currentGender) {
            val page = genders.indexOf(currentGender)
            pagerState.animateScrollToPage(page)
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            CategoryList(
                gender = genders[page],
                state = categoryListState,
                itemsState = categoryListItemsState,
                onCategoryClicked = onCategoryClicked,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @Composable
    private fun CategoryList(
        gender: GenderTab,
        state: CategoryListState,
        itemsState: CategoryListItemsState,
        onCategoryClicked: (Category) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = state,
            contentKey = { getCategoryListContentKey(it) },
            modifier = modifier,
        ) { state ->
            when (state) {
                is CategoryListState.Success -> {
                    val categories = when (gender) {
                        GenderTab.WOMEN -> state.womenCategories
                        GenderTab.MEN -> state.menCategories
                    }

                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        categories.forEachIndexed { index, categoryItem ->
                            if (categoryItem.category.id in itemsState.visibleCategoryIds) {
                                item(key = categoryItem.category.id.value) {
                                    CategoryItem(
                                        categoryItem = categoryItem,
                                        onCategoryClicked = onCategoryClicked,
                                    )

                                    if (index != categories.lastIndex) {
                                        Divider(
                                            color = UiKitTheme.colorsReworked.border.general.default,
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

                CategoryListState.Loading -> {
                    CategoryListSkeleton(modifier = Modifier.fillMaxSize())
                }

                is CategoryListState.Error -> {
                    // TODO: [High] Implement
                }
            }
        }
    }

    @Composable
    private fun CategoryItem(
        categoryItem: CategoryItem,
        onCategoryClicked: (Category) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = CategoryItemMinHeight)
                .clickable { onCategoryClicked(categoryItem.category) }
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .padding(start = 20.dp * categoryItem.nestingLevel),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f),
            ) {
                val color = categoryItem.category.color?.toComposeColor()
                    ?: UiKitTheme.colorsReworked.text.general.regular.default

                // TODO: [High] Make multiline?
                Text(
                    text = categoryItem.category.name.uppercase(),
                    style = UiKitTheme.typographyReworked.tertiary.light,
                    color = color,
                    maxLines = 1,
                )

                if (categoryItem.category.label != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = categoryItem.category.label.uppercase(),
                        style = UiKitTheme.typographyReworked.caption2.light,
                        color = color,
                        maxLines = 1,
                        modifier = Modifier.align(Alignment.Top),
                    )
                }
            }

            if (categoryItem.isExpandable) {
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    painter = painterResource(R.drawable.ic_small_arrow_up_24),
                    contentDescription = null, // TODO: [High] Add content description
                    modifier = Modifier.size(16.dp),
                )
            }
        }
    }

    @Composable
    private fun CategoryListSkeleton(
        modifier: Modifier = Modifier,
    ) {
        val shimmer = rememberSkeletonShimmer(ShimmerBounds.Window)

        LazyColumn(modifier = modifier) {
            items(
                count = CategoryListSkeletonItemCount,
                key = { it },
            ) { index ->
                CategoryListSkeletonItem(
                    index = index,
                    shimmer = shimmer,
                )

                if (index != CategoryListSkeletonItemCount - 1) {
                    Divider(
                        color = UiKitTheme.colorsReworked.border.general.default,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
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
                .heightIn(min = CategoryItemMinHeight)
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
            val height = 16.dp
            val shape = remember { RoundedCornerShape(2.dp) }

            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .fillMaxWidth(widthFraction)
                    .height(height)
                    .clip(shape)
                    .shimmer(shimmer)
                    .background(UiKitTheme.colorsReworked.background.skeleton),
            )

            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(height)
                    .clip(shape)
                    .shimmer(shimmer)
                    .background(UiKitTheme.colorsReworked.background.skeleton),
            )
        }
    }

    private fun getCategoryListContentKey(state: CategoryListState): Any {
        return when (state) {
            CategoryListState.Loading -> state
            is CategoryListState.Success -> CategoryListContentKeySuccess
            is CategoryListState.Error -> state
        }
    }

    private const val CategoryListContentKeySuccess = "CategoryListContentKeySuccess"

    private val CategoryItemMinHeight: Dp get() = 56.dp

    private const val CategoryListSkeletonItemCount = 20
}

package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.Shimmer
import com.valentinilk.shimmer.ShimmerBounds
import com.valentinilk.shimmer.shimmer
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.TopBarDefaults
import ru.zarina.zarina.ui.common.component.ZarinaTabIndicator
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.skeleton.rememberSkeletonShimmer
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextField
import ru.zarina.zarina.ui.common.component.textfield.ZarinaTextFieldDefaults
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.CategoryListState
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.GenderPickerTab
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
        tabs: List<GenderPickerTab>,
        currentTab: GenderPickerTab,
        onTabClicked: (GenderPickerTab) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val selectedTabIndex = remember(tabs, currentTab) {
            tabs.indexOf(currentTab)
        }

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
            tabs.forEach { tab ->
                ZarinaButton(
                    onClick = { onTabClicked(tab) },
                    size = ZarinaButtonSize.Medium,
                    colors = ZarinaButtonDefaults.backlessColors(),
                    contentPadding = ZarinaButtonDefaults.ContentPaddingEven,
                ) {
                    val textResId = when (tab) {
                        GenderPickerTab.FOR_WOMEN -> R.string.for_women
                        GenderPickerTab.FOR_MEN -> R.string.for_men
                    }

                    val style = if (tab == currentTab) {
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
        genderPickerTabs: List<GenderPickerTab>,
        currentGenderPickerTab: GenderPickerTab,
        categoryListState: CategoryListState,
        modifier: Modifier = Modifier,
    ) {
        val pagerState = rememberPagerState(
            initialPage = remember { genderPickerTabs.indexOf(currentGenderPickerTab) },
            pageCount = { genderPickerTabs.size },
        )

        LaunchedEffect(pagerState, genderPickerTabs, currentGenderPickerTab) {
            val page = genderPickerTabs.indexOf(currentGenderPickerTab)
            pagerState.animateScrollToPage(page)
        }

        HorizontalPager(
            state = pagerState,
            userScrollEnabled = false,
            modifier = modifier,
        ) { page ->
            CategoryList(
                state = categoryListState,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

    @Composable
    private fun CategoryList(
        state: CategoryListState,
        modifier: Modifier = Modifier,
    ) {
        // TODO: [High] Specify content key
        Crossfade(
            targetState = state,
            modifier = modifier,
        ) { state ->
            when (state) {
                is CategoryListState.Success -> {
                    // TODO: [High] Implement
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
                    isDividerVisible = index != CategoryListSkeletonItemCount - 1,
                )
            }
        }
    }

    @Composable
    private fun CategoryListSkeletonItem(
        index: Int,
        shimmer: Shimmer,
        isDividerVisible: Boolean,
        modifier: Modifier = Modifier,
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(56.dp) // TODO: [High] Extract?
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

            if (isDividerVisible) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(UiKitTheme.colorsReworked.border.general.default),
                )
            }
        }
    }

    private const val CategoryListSkeletonItemCount = 20
}

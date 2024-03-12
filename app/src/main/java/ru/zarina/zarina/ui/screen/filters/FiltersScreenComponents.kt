package ru.zarina.zarina.ui.screen.filters

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import ru.zarina.zarina.R
import ru.zarina.zarina.domain.filter.Filter
import ru.zarina.zarina.domain.filter.Filters
import ru.zarina.zarina.domain.filter.ListFilter
import ru.zarina.zarina.domain.filter.ListFilterItem
import ru.zarina.zarina.domain.filter.PriceFilter
import ru.zarina.zarina.domain.filter.SortFilterItem
import ru.zarina.zarina.domain.filter.ToggleFilter
import ru.zarina.zarina.domain.filter.sorting
import ru.zarina.zarina.ui.common.component.Counter
import ru.zarina.zarina.ui.common.component.PriceFilter
import ru.zarina.zarina.ui.common.component.ZarinaSwitch
import ru.zarina.zarina.ui.common.component.button.ZarinaBackIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.skeleton.ZarinaSkeleton
import ru.zarina.zarina.ui.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.common.util.domain.nameResId
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.FilterListState
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultTransitionSpec
import ru.zarina.zarina.util.compose.Crossfade

object FiltersScreenComponents {

    @Composable
    fun TopBar(
        isResetButtonVisible: Boolean,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                ZarinaBackIconButton(
                    onClick = actions.onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.filters),
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isResetButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = actions.onResetClicked,
                        size = ZarinaButtonSize.Small,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.reset).uppercase(),
                            style = UiKitTheme.typography.caption1.regular,
                        )
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Composable
    fun FilterList(
        state: FilterListState,
        onFilterChanged: (Filter) -> Unit,
        onFilterClicked: (Filter) -> Unit,
        onShowProductsClicked: () -> Unit,
        productCount: Int?,
        onFilterListErrorRefreshClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Crossfade(
            targetState = state,
            contentKey = {
                when (it) {
                    is FilterListState.FilterList -> FilterListContentKey
                    FilterListState.Loading, is FilterListState.Error -> it
                }
            },
            modifier = modifier,
        ) { state ->
            when (state) {
                is FilterListState.FilterList -> {
                    Filters(
                        filters = state.filters,
                        onFilterChanged = onFilterChanged,
                        onFilterClicked = onFilterClicked,
                        onShowProductsClicked = onShowProductsClicked,
                        productCount = productCount,
                        modifier = Modifier.fillMaxSize(),
                    )
                }

                FilterListState.Loading -> {
                    FiltersSkeleton(modifier = Modifier.fillMaxSize())
                }

                is FilterListState.Error -> {
                    ZarinaErrorScreen(
                        state = state.errorState,
                        onRefreshClicked = onFilterListErrorRefreshClicked,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    )
                }
            }
        }
    }

    @Composable
    private fun Filters(
        filters: Filters,
        onFilterChanged: (Filter) -> Unit,
        onFilterClicked: (Filter) -> Unit,
        onShowProductsClicked: () -> Unit,
        productCount: Int?,
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val scrollState = rememberScrollState()
            val isDragged = scrollState.interactionSource.collectIsDraggedAsState()
            val keyboardController = LocalSoftwareKeyboardController.current

            LaunchedEffect(scrollState, keyboardController) {
                snapshotFlow { isDragged.value }
                    .collect { keyboardController?.hide() }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(scrollState),
            ) {
                val filterCount = remember(filters) { filters.iterator().asSequence().count() }

                filters.forEachIndexed { index, filter ->
                    key(filter.type) {
                        when (filter) {
                            is PriceFilter -> {
                                val horizontalPadding = 16.dp
                                val systemGestureHorizontalPadding = WindowInsets.safeGestures
                                    .asPaddingValues()
                                    .calculateLeftPadding(LocalLayoutDirection.current)
                                val sliderHorizontalPadding =
                                    (systemGestureHorizontalPadding - horizontalPadding)
                                        .coerceAtLeast(0.dp)

                                PriceFilter(
                                    filter = filter,
                                    onFilterChanged = { onFilterChanged(it) },
                                    sliderAdditionalHorizontalPadding = sliderHorizontalPadding,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(
                                            start = horizontalPadding,
                                            top = 16.dp,
                                            end = horizontalPadding,
                                            bottom = 8.dp,
                                        ),
                                )
                            }

                            is ListFilter<*> -> {
                                if (filter.isSingleSelection) {
                                    SingleSelectionFilterItem(
                                        type = filter.type,
                                        selected = remember(filter.selectedItems) {
                                            filter.selectedItems.firstOrNull()
                                        },
                                        onClick = { onFilterClicked(filter) },
                                    )
                                } else {
                                    MultiSelectionFilterItem(
                                        type = filter.type,
                                        selectedCount = remember(filter.selectedItems) {
                                            filter.selectedItems.size
                                        },
                                        onClick = { onFilterClicked(filter) },
                                    )
                                }
                            }

                            is ToggleFilter -> {
                                ToggleFilterItem(
                                    type = filter.type,
                                    isChecked = filter.isEnabled,
                                    onCheckedChanged = {
                                        val updatedFilter = filter.copy(isEnabled = it)
                                        onFilterChanged(updatedFilter)
                                    },
                                )
                            }
                        }

                        if (filter !is PriceFilter && index < filterCount - 1) {
                            Divider(
                                color = UiKitTheme.colors.background.skeleton,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp),
                            )
                        }
                    }
                }
            }

            Divider(
                color = UiKitTheme.colors.background.skeleton,
                modifier = Modifier.fillMaxWidth(),
            )

            ShowProductsButton(
                onClick = onShowProductsClicked,
                productCount = productCount,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            )
        }
    }

    @Composable
    private fun FiltersSkeleton(
        modifier: Modifier = Modifier,
    ) {
        Column(modifier = modifier) {
            val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
            repeat(FilterSkeletonItemCount) { index ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = FilterItemMinHeight)
                        .padding(horizontal = 16.dp),
                ) {
                    val height = 16.dp
                    ZarinaSkeleton(
                        shimmer = shimmer,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .fillMaxWidth(fraction = 0.55f)
                            .height(height),
                    )

                    ZarinaSkeleton(
                        shimmer = shimmer,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(height),
                    )
                }

                if (index < FilterSkeletonItemCount - 1) {
                    Divider(
                        color = UiKitTheme.colors.background.skeleton,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }

    @Composable
    private fun SingleSelectionFilterItem(
        type: Filter.Type,
        selected: ListFilterItem?,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FilterItemMinHeight)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(type.nameResId),
                style = FilterTitleTextStyle,
                color = FilterTitleColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            val selectedText = when (selected) {
                is SortFilterItem -> stringResource(selected.sorting.nameResId)
                else -> ""
            }
            Text(
                text = selectedText,
                style = UiKitTheme.typography.secondary.light,
                color = UiKitTheme.colors.text.general.regular.muted,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            FilterItemEndArrowIcon()
        }
    }

    @Composable
    private fun MultiSelectionFilterItem(
        type: Filter.Type,
        selectedCount: Int,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FilterItemMinHeight)
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(type.nameResId),
                style = FilterTitleTextStyle,
                color = FilterTitleColor,
            )

            Spacer(modifier = Modifier.width(8.dp))

            if (selectedCount > 0) {
                Counter(
                    value = selectedCount.toString(),
                    textStyle = UiKitTheme.typography.footnote.bold,
                    contentPadding = PaddingValues(start = 8.dp, top = 1.dp, end = 8.dp),
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            FilterItemEndArrowIcon()
        }
    }

    @Composable
    private fun ToggleFilterItem(
        type: Filter.Type,
        isChecked: Boolean,
        onCheckedChanged: (Boolean) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .heightIn(min = FilterItemMinHeight)
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            Text(
                text = stringResource(type.nameResId),
                style = FilterTitleTextStyle,
                color = FilterTitleColor,
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(16.dp))

            ZarinaSwitch(
                isChecked = isChecked,
                onCheckedChanged = onCheckedChanged,
            )
        }
    }

    @Composable
    private fun ShowProductsButton(
        onClick: () -> Unit,
        productCount: Int?,
        modifier: Modifier = Modifier,
    ) {
        ZarinaButton(
            onClick = onClick,
            isEnabled = productCount == null || productCount > 0,
            modifier = modifier,
        ) {
            AnimatedContent(
                targetState = productCount,
                transitionSpec = {
                    AnimatedContentDefaultTransitionSpec().using(sizeTransform = null)
                },
                contentAlignment = Alignment.Center,
                label = "ShowProductsButton",
            ) { productCount ->
                val text = when {
                    productCount == null -> stringResource(R.string.show_products)
                    productCount > 0 -> {
                        pluralStringResource(
                            R.plurals.show_products,
                            productCount,
                            productCount,
                        )
                    }

                    else -> stringResource(R.string.products_not_found)
                }

                Text(text = text.uppercase())
            }
        }
    }

    @Composable
    private fun FilterItemEndArrowIcon(
        modifier: Modifier = Modifier,
    ) {
        Icon(
            painter = painterResource(R.drawable.ic_small_arrow_up_24),
            contentDescription = null,
            modifier = modifier
                .size(16.dp)
                .rotate(degrees = 90f),
        )
    }

    @Stable
    class TopBarActions(
        val onBackClicked: () -> Unit,
        val onResetClicked: () -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopBarActions

            if (onBackClicked != other.onBackClicked) return false
            return onResetClicked == other.onResetClicked
        }

        override fun hashCode(): Int {
            var result = onBackClicked.hashCode()
            result = 31 * result + onResetClicked.hashCode()
            return result
        }
    }

    private const val FilterListContentKey = "FilterListContentKey"

    private const val FilterSkeletonItemCount = 10

    private val FilterItemMinHeight: Dp get() = 56.dp

    private val FilterTitleTextStyle: TextStyle
        @Composable
        get() = UiKitTheme.typography.secondary.light

    private val FilterTitleColor: Color
        @Composable
        get() = UiKitTheme.colors.text.general.regular.default
}

package ru.livetyping.zarina.feature.productlist.ui.impl.productlist.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.uicompose.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.tab.ZarinaBracketTab
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBarDefaults
import ru.livetyping.zarina.feature.productlist.ui.impl.productlist.model.SubcategoryListState
import kotlin.random.Random
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    categoryName: String?,
    subcategoryListState: SubcategoryListState,
    onBackClicked: () -> Unit,
    onSeeAllProductsInCategoryClicked: () -> Unit,
    onSubcategoryClicked: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.padding(vertical = ZarinaTopBarDefaults.LargeVerticalPadding)) {
        Header(
            categoryName = categoryName,
            onBackClicked = onBackClicked,
        )

        SubcategoryList(
            state = subcategoryListState,
            onSeeAllClicked = onSeeAllProductsInCategoryClicked,
            onSubcategoryClicked = onSubcategoryClicked,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun Header(
    categoryName: String?,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(
                onClick = onBackClicked,
                modifier = Modifier.padding(start = 2.dp),
            )
        },
        centerContent = {
            Crossfade(
                targetState = categoryName,
                label = "TopBar category name",
                modifier = Modifier.fillMaxWidth(),
            ) { categoryName ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    val textStyle = UiKitTheme2.typography.h3

                    if (categoryName != null) {
                        Text(
                            text = categoryName.uppercase(),
                            style = textStyle,
                            color = UiKitTheme2.colors.mainBlack,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    } else {
                        ZarinaTextSkeleton(
                            textStyle = textStyle,
                            modifier = Modifier.fillMaxWidth(0.6f),
                        )
                    }
                }
            }
        },
        contentPadding = PaddingValues(),
        modifier = modifier,
    )
}

@Suppress("NAME_SHADOWING")
@Composable
private fun SubcategoryList(
    state: SubcategoryListState,
    onSeeAllClicked: () -> Unit,
    onSubcategoryClicked: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            when (targetState) {
                SubcategoryListState.Empty -> {
                    (expandVertically() + fadeIn() togetherWith shrinkVertically() + fadeOut())
                        .using(SizeTransform(clip = false))
                }

                else -> AnimatedContentCrossfadeTransitionSpec
            }
        },
        contentAlignment = Alignment.Center,
        contentKey = {
            when (it) {
                is SubcategoryListState.Success -> SubcategoryListContentKey.Success
                SubcategoryListState.Empty -> it
                SubcategoryListState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is SubcategoryListState.Success -> {
                SubcategoryListSuccess(
                    state = state,
                    onSeeAllClicked = onSeeAllClicked,
                    onSubcategoryClicked = onSubcategoryClicked,
                )
            }

            SubcategoryListState.Loading -> {
                SubcategoryListLoading()
            }

            SubcategoryListState.Empty -> Unit
        }
    }
}

@Composable
private fun SubcategoryListSuccess(
    state: SubcategoryListState.Success,
    onSeeAllClicked: () -> Unit,
    onSubcategoryClicked: (Category) -> Unit,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()

    LaunchedEffect(state.selectedCategoryId) {
        if (state.selectedCategoryId == null) {
            lazyListState.animateScrollToItem(0)
        }
    }

    LazyRow(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = SubcategoryListHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(
            space = SubcategoryListSpacedBy,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = modifier,
    ) {
        item(key = SeeAllProductsKey) {
            ZarinaBracketTab(
                text = stringResource(RCommon.string.res_all).uppercase(),
                isSelected = state.selectedCategoryId == null,
                onClick = onSeeAllClicked,
            )
        }

        items(
            items = state.categories,
            key = { it.id.value },
        ) { category ->
            ZarinaBracketTab(
                text = category.name.uppercase(),
                isSelected = category.id == state.selectedCategoryId,
                onClick = { onSubcategoryClicked(category) },
            )
        }
    }
}

@Composable
private fun SubcategoryListLoading(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer()

    LazyRow(
        contentPadding = PaddingValues(horizontal = SubcategoryListHorizontalPadding),
        horizontalArrangement = Arrangement.spacedBy(
            space = SubcategoryListSpacedBy,
            alignment = Alignment.CenterHorizontally,
        ),
        modifier = modifier,
    ) {
        items(count = 8) {
            ZarinaBracketTab(isSelected = false) {
                val width = remember {
                    val fraction = Random.nextFloat()
                    val additionalWidth = lerp(0.dp, 48.dp, fraction)
                    48.dp + additionalWidth
                }

                ZarinaTextSkeleton(
                    textStyle = UiKitTheme2.typography.body,
                    shimmer = shimmer,
                    modifier = Modifier.width(width),
                )
            }
        }
    }
}

private enum class SubcategoryListContentKey { Success }

private val SubcategoryListHorizontalPadding: Dp get() = 8.dp
private val SubcategoryListSpacedBy: Dp get() = 12.dp

private const val SeeAllProductsKey = "AllSubcategoryKey"

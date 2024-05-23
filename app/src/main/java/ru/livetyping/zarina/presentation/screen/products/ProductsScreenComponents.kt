package ru.livetyping.zarina.presentation.screen.products

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.domain.category.Category
import ru.livetyping.zarina.presentation.common.component.button.ZarinaBackIconButton
import ru.livetyping.zarina.presentation.common.component.button.ZarinaIconButton
import ru.livetyping.zarina.presentation.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.presentation.common.component.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.presentation.common.component.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.presentation.common.component.tag.ZarinaTag
import ru.livetyping.zarina.presentation.common.component.tag.ZarinaTagSkeleton
import ru.livetyping.zarina.presentation.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.presentation.common.error.rememberErrorState
import ru.livetyping.zarina.presentation.screen.products.ProductsViewModel.TagListState
import ru.livetyping.zarina.presentation.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.util.compose.text.unscalable

@Suppress("ConstPropertyName")
object ProductsScreenComponents {

    @Composable
    fun TopBar(
        title: String?,
        appliedFilterCount: Int,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .heightIn(min = TopBarDefaults.MinHeight)
                .padding(vertical = TopBarDefaults.VerticalPadding),
        ) {
            ZarinaBackIconButton(
                onClick = actions.onBackClicked,
                iconSize = TopBarIconSize,
                modifier = Modifier.padding(start = 2.dp),
            )

            Spacer(modifier = Modifier.width(4.dp))

            AnimatedContent(
                targetState = title,
                transitionSpec = { AnimatedContentCrossfadeTransitionSpec() },
                contentAlignment = Alignment.CenterStart,
                label = "TopBar title",
                modifier = Modifier.weight(1f),
            ) { title ->
                val textStyle = UiKitTheme.typography.primary.regular
                if (title != null) {
                    Text(
                        text = title,
                        style = textStyle,
                        color = UiKitTheme.colors.text.general.regular.default,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                } else {
                    ZarinaTextSkeleton(
                        textStyle = textStyle,
                        modifier = Modifier
                            .wrapContentWidth(align = Alignment.Start)
                            .fillMaxWidth(fraction = 0.5f),
                    )
                }
            }

            Spacer(modifier = Modifier.width(4.dp))

            ZarinaIconButton(
                onClick = actions.onSearchClicked,
                indication = ripple(bounded = false, radius = TopBarIconSize),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_magnifying_glass_24),
                    contentDescription = stringResource(R.string.search),
                    tint = UiKitTheme.colors.icon.regular.default,
                    modifier = Modifier.size(TopBarIconSize),
                )
            }

            Box {
                ZarinaIconButton(
                    onClick = actions.onFiltersClicked,
                    indication = ripple(bounded = false, radius = TopBarIconSize),
                    modifier = Modifier.padding(end = 2.dp),
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_settings_menu_24),
                        contentDescription = stringResource(R.string.filters),
                        tint = UiKitTheme.colors.icon.regular.default,
                        modifier = Modifier.size(TopBarIconSize),
                    )
                }

                AppliedFilterCounter(
                    appliedFilterCount = appliedFilterCount,
                    modifier = Modifier.align(AppliedFilterCounterAlignment),
                )
            }
        }
    }

    @Composable
    fun Tags(
        state: TagListState?,
        selectedTagId: Category.Id?,
        onTagClicked: (Category) -> Unit,
        modifier: Modifier = Modifier,
    ) {
        @Suppress("NAME_SHADOWING")
        AnimatedContent(
            targetState = state,
            transitionSpec = {
                if (initialState != null && targetState != null) {
                    AnimatedContentCrossfadeTransitionSpec()
                } else {
                    AnimatedContentDefaultTransitionSpec()
                }.using(SizeTransform(clip = false))
            },
            contentAlignment = Alignment.CenterStart,
            contentKey = {
                when (it) {
                    is TagListState.TagList -> TagListContentKeyTagList
                    TagListState.Loading -> it
                    null -> it
                }
            },
            label = "Tags",
            modifier = modifier,
        ) { state ->
            val horizontalArrangement = Arrangement.spacedBy(8.dp)
            val contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 8.dp)

            when (state) {
                is TagListState.TagList -> {
                    LazyRow(
                        horizontalArrangement = horizontalArrangement,
                        contentPadding = contentPadding,
                    ) {
                        items(
                            items = state.tags,
                            key = { it.id.value },
                        ) { tag ->
                            ZarinaTag(
                                onClick = { onTagClicked(tag) },
                                isSelected = tag.id == selectedTagId,
                            ) {
                                Text(text = tag.name)
                            }
                        }
                    }
                }

                TagListState.Loading -> {
                    val skeletonShimmer = rememberZarinaSkeletonShimmer()
                    LazyRow(
                        horizontalArrangement = horizontalArrangement,
                        contentPadding = contentPadding,
                    ) {
                        items(count = 10) {
                            ZarinaTagSkeleton(shimmer = skeletonShimmer)
                        }
                    }
                }

                null -> Unit
            }
        }
    }

    @Composable
    fun ProductsNotFoundPlaceholder(
        modifier: Modifier = Modifier,
    ) {
        val state = rememberErrorState(
            iconResId = R.drawable.ic_magnifying_glass_64,
            title = stringResource(R.string.could_not_find_products),
            body = stringResource(R.string.try_select_another_category),
            isButtonVisible = false,
        )

        ZarinaErrorScreen(
            state = state,
            onButtonClicked = {},
            modifier = modifier,
        )
    }

    @Composable
    private fun AppliedFilterCounter(
        appliedFilterCount: Int,
        modifier: Modifier = Modifier,
    ) {
        if (appliedFilterCount > 0) {
            Text(
                text = appliedFilterCount.toString(),
                style = UiKitTheme.typography.caption2.bold.unscalable(LocalDensity.current),
                color = UiKitTheme.colors.text.general.inversed.default,
                modifier = modifier
                    .background(
                        color = UiKitTheme.colors.background.general.inversed.default,
                        shape = CircleShape,
                    )
                    .padding(start = 6.dp, top = 1.dp, end = 6.dp),
            )
        }
    }

    @Stable
    class TopBarActions(
        val onBackClicked: () -> Unit,
        val onSearchClicked: () -> Unit,
        val onFiltersClicked: () -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopBarActions

            if (onBackClicked != other.onBackClicked) return false
            if (onSearchClicked != other.onSearchClicked) return false
            return onFiltersClicked == other.onFiltersClicked
        }

        override fun hashCode(): Int {
            var result = onBackClicked.hashCode()
            result = 31 * result + onSearchClicked.hashCode()
            result = 31 * result + onFiltersClicked.hashCode()
            return result
        }
    }

    private val TopBarIconSize: Dp get() = 20.dp

    private const val TagListContentKeyTagList = "TagListContentKeyTagList"

    private val AppliedFilterCounterAlignment: Alignment
        get() = BiasAlignment(0.5f, -0.5f)
}

package ru.livetyping.zarina.feature.productlist.ui.impl.impl.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uicompose.AnimatedContentCrossfadeTransitionSpec
import ru.livetyping.zarina.core.uicompose.AnimatedContentDefaultTransitionSpec
import ru.livetyping.zarina.core.uikit.list.ZarinaListDefaults.animateZarinaItem
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.tag.ZarinaTag
import ru.livetyping.zarina.core.uikit.tag.ZarinaTagSkeleton
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TagListEvent
import ru.livetyping.zarina.feature.productlist.ui.impl.impl.model.TagListState

@Composable
internal fun TagList(
    state: TagListState,
    onEvent: (TagListEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    @Suppress("NAME_SHADOWING")
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            if (initialState != TagListState.Empty && targetState != TagListState.Empty) {
                AnimatedContentCrossfadeTransitionSpec
            } else {
                AnimatedContentDefaultTransitionSpec
            }.using(SizeTransform(clip = false))
        },
        contentAlignment = Alignment.CenterStart,
        contentKey = {
            when (it) {
                is TagListState.Success -> ContentKey.Success
                TagListState.Empty -> it
                TagListState.Loading-> it
            }
        },
        label = "TagList",
        modifier = modifier,
    ) { state ->
        val horizontalArrangement = Arrangement.spacedBy(8.dp)
        val contentPadding = PaddingValues(
            start = 16.dp,
            end = 16.dp,
            bottom = 8.dp
        )

        when (state) {
            is TagListState.Success -> {
                val lazyListState = rememberLazyListState()
                DisposableEffect(state.tags) {
                    lazyListState.requestScrollToItem(0)
                    onDispose {}
                }

                LazyRow(
                    state = lazyListState,
                    horizontalArrangement = horizontalArrangement,
                    contentPadding = contentPadding,
                ) {
                    items(
                        items = state.tags,
                        key = { it.id.value },
                    ) { tag ->
                        ZarinaTag(
                            onClick = { onEvent(TagListEvent.TagClicked(tag)) },
                            isSelected = tag.id == state.selectedTagId,
                            modifier = Modifier.animateZarinaItem(this),
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
                        ZarinaTagSkeleton(
                            shimmer = skeletonShimmer,
                            modifier = Modifier.animateZarinaItem(this),
                        )
                    }
                }
            }

            TagListState.Empty -> Unit
        }
    }
}

private enum class ContentKey { Success }

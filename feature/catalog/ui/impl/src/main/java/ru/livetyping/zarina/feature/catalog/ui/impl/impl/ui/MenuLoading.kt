package ru.livetyping.zarina.feature.catalog.ui.impl.impl.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer

// TODO: [Medium] Add banner skeleton

@Composable
internal fun MenuLoading(
    bottomPadding: Dp,
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer()

    LazyColumn(
        contentPadding = PaddingValues(bottom = bottomPadding),
        modifier = modifier,
    ) {
        items(4) {
            MenuItemBasicSkeleton(shimmer)
        }

        item {
            Spacer(modifier = Modifier.height(MenuSpacerHeightMedium))
        }

        items(8) {
            MenuItemBasicSkeleton(shimmer)
        }

        item {
            Spacer(modifier = Modifier.height(MenuSpacerHeightMedium))
        }

        items(3) {
            MenuItemBasicSkeleton(shimmer)
        }
    }
}

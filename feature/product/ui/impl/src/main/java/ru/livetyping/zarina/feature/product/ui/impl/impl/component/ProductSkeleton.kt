package ru.livetyping.zarina.feature.product.ui.impl.impl.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme

@Composable
internal fun ProductSkeleton(
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.View)

    LazyColumn(
        contentPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding),
        modifier = modifier,
    ) {
        item {
            ZarinaSkeleton(
                shimmer = shimmer,
                shape = RectangleShape,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(MediaPagerAspectRatio),
            )
        }

        item {
            ProductGeneralInfoSkeleton(
                shimmer = shimmer,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            )
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.light,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                )
                Spacer(modifier = Modifier.weight(1f))
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.size(12.dp),
                )
            }
        }

        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .heightIn(min = 56.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.light,
                    shimmer = shimmer,
                    modifier = Modifier.fillMaxWidth(fraction = 0.5f),
                )
                Spacer(modifier = Modifier.weight(1f))
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier.size(12.dp),
                )
            }
        }

        item {
            Box(modifier = Modifier.padding(16.dp)) {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.bold,
                    shimmer = shimmer,
                    modifier = Modifier.width(80.dp),
                )
            }
        }

        item {
            SuggestedProductsSkeleton(
                shimmer = shimmer,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }

        item {
            Box(modifier = Modifier.padding(16.dp)) {
                ZarinaTextSkeleton(
                    textStyle = UiKitTheme.typography.secondary.bold,
                    shimmer = shimmer,
                    modifier = Modifier.width(80.dp),
                )
            }
        }

        item {
            SuggestedProductsSkeleton(
                shimmer = shimmer,
                contentPadding = PaddingValues(horizontal = 16.dp),
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

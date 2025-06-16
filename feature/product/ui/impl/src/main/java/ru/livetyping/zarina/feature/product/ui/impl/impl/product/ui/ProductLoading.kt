package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.product.ProductDefaults
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.ZarinaTextSkeleton
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ProductLoading(
    windowInsetsProvider: @Composable () -> WindowInsets,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    val shimmer = rememberZarinaSkeletonShimmer()
    val contentPadding = PaddingValues(
        top = windowInsetsProvider().asPaddingValues().calculateTopPadding(),
        bottom = bottomPaddingProvider() + ZarinaScrollableDefaults.ScrollableBottomPadding,
    )

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        item {
            ZarinaSkeleton(
                shimmer = shimmer,
                shape = RectangleShape,
                modifier = Modifier.aspectRatio(ProductDefaults.MediaAspectRatio),
            )
        }

        item {
            ZarinaTextSkeleton(
                textStyle = UiKitTheme2.typography.h4,
                shimmer = shimmer,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth(0.8f),
            )
        }

        item {
            ZarinaTextSkeleton(
                text = "1999",
                textStyle = UiKitTheme2.typography.h2Regular,
                shimmer = shimmer,
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp),
            )
        }

        item {
            Row(
                modifier = Modifier
                    .padding(top = 10.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ZarinaTextSkeleton(
                    text = stringResource(R.string.product_podeli_price, 600, 4),
                    textStyle = UiKitTheme2.typography.body2,
                    shimmer = shimmer,
                )

                Spacer(modifier = Modifier.weight(1f))

                ZarinaTextSkeleton(
                    text = pluralStringResource(RCommon.plurals.res_bonus_count, 200, "200"),
                    textStyle = UiKitTheme2.typography.body2,
                    shimmer = shimmer,
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                )

                Spacer(modifier = Modifier.height(8.dp))

                ZarinaSkeleton(
                    shimmer = shimmer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                )
            }
        }

        item {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp),
            ) {
                ZarinaTextSkeleton(
                    text = stringResource(R.string.product_color_selector_header),
                    textStyle = UiKitTheme2.typography.body,
                    shimmer = shimmer,
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    repeat(4) {
                        ZarinaSkeleton(
                            shimmer = shimmer,
                            modifier = Modifier
                                .width(60.dp)
                                .aspectRatio(ProductDefaults.MediaAspectRatio),
                        )
                    }
                }
            }
        }

        item {
            ZarinaSkeleton(
                shimmer = shimmer,
                shape = RectangleShape,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .aspectRatio(ProductDefaults.MediaAspectRatio),
            )
        }
    }
}

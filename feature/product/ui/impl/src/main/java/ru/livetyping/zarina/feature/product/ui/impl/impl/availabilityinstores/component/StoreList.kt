package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uicompose.plus
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.product.ui.impl.R
import ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model.StoreListState

@Suppress("NAME_SHADOWING")
@Composable
internal fun StoreList(
    state: StoreListState,
    onErrorRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Crossfade(
        targetState = state,
        contentKey = { state ->
            when (state) {
                is StoreListState.Success -> StoreListContentKey.Success
                StoreListState.Empty -> state
                StoreListState.Loading -> state
                is StoreListState.Error -> state
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is StoreListState.Success -> {
                StoreListSuccess(
                    state = state,
                    windowInsetsProvider = windowInsetsProvider,
                )
            }

            StoreListState.Empty -> {
                StoreListEmpty(
                    modifier = Modifier
                        .windowInsetsPadding(windowInsetsProvider())
                        .padding(16.dp),
                )
            }

            StoreListState.Loading -> {
                StoreListLoading(windowInsetsProvider)
            }

            is StoreListState.Error -> {
                ZarinaErrorScreen(
                    state = state.state,
                    onButtonClicked = onErrorRefreshClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(windowInsetsProvider())
                        .padding(16.dp),
                )
            }
        }
    }
}

@Composable
private fun StoreListSuccess(
    state: StoreListState.Success,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val windowInsetsPadding = windowInsetsProvider()
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
    val bottomPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding)
    val contentPadding = windowInsetsPadding.plus(bottomPadding, LocalLayoutDirection.current)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = state.availabilityList,
            key = { _, item -> item.store.id.value },
        ) { index, availability ->
            StoreItem(availability)

            if (index < state.availabilityList.lastIndex) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

@Composable
private fun StoreListEmpty(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.product_is_not_available),
            style = UiKitTheme.typography.primary.regular,
            color = UiKitTheme.colors.text.general.regular.default,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun StoreListLoading(
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    val windowInsetsPadding = windowInsetsProvider()
        .only(WindowInsetsSides.Bottom)
        .asPaddingValues()
    val bottomPadding = PaddingValues(bottom = ZarinaScrollableDefaults.ScrollableBottomPadding)
    val contentPadding = windowInsetsPadding.plus(bottomPadding, LocalLayoutDirection.current)

    val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)

    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier,
    ) {
        items(StoreListLoadingItemCount) { index ->
            StoreItemSkeleton(shimmer)

            if (index < StoreListLoadingItemCount - 1) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}

private enum class StoreListContentKey { Success }

private const val StoreListLoadingItemCount = 10

package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.valentinilk.shimmer.ShimmerBounds
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryMethod
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.skeleton.rememberZarinaSkeletonShimmer
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliverymethodselector.model.DeliveryMethodSelectorState

@Composable
internal fun DeliveryMethodSelector(
    state: DeliveryMethodSelectorState,
    onMethodClicked: (DeliveryMethod) -> Unit,
    onErrorRefreshClicked: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    @Suppress("NAME_SHADOWING")
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is DeliveryMethodSelectorState.Success -> DeliveryMethodSelectorContentKey.Success
                is DeliveryMethodSelectorState.Error -> it
                DeliveryMethodSelectorState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is DeliveryMethodSelectorState.Success -> {
                DeliveryMethodSelectorSuccess(
                    state = state,
                    onDeliveryMethodClicked = onMethodClicked,
                    windowInsetsProvider = windowInsetsProvider,
                )
            }

            DeliveryMethodSelectorState.Loading -> {
                DeliveryMethodSelectorLoading(windowInsetsProvider = windowInsetsProvider)
            }

            is DeliveryMethodSelectorState.Error -> {
                ZarinaErrorScreen(
                    state = state.errorState,
                    onButtonClicked = onErrorRefreshClicked,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .windowInsetsPadding(windowInsetsProvider()),
                )
            }
        }
    }
}

@Composable
private fun DeliveryMethodSelectorSuccess(
    state: DeliveryMethodSelectorState.Success,
    onDeliveryMethodClicked: (DeliveryMethod) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        state.deliveryMethods.forEachIndexed { index, method ->
            key(method.id.value) {
                DeliveryMethod(
                    method = method,
                    onClick = onDeliveryMethodClicked,
                )

                if (index < state.deliveryMethods.lastIndex) {
                    ZarinaDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    )
                }
            }
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
    }
}

@Composable
private fun DeliveryMethodSelectorLoading(
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.verticalScroll(rememberScrollState())) {
        val shimmer = rememberZarinaSkeletonShimmer(ShimmerBounds.Window)
        repeat(DeliveryMethodSelectorLoadingCount) { index ->
            DeliveryMethodSkeleton(shimmer = shimmer)

            if (index < DeliveryMethodSelectorLoadingCount - 1) {
                ZarinaDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }

        Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
        Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
    }
}

private enum class DeliveryMethodSelectorContentKey { Success }

private const val DeliveryMethodSelectorLoadingCount = 6

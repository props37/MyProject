package ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.domain.model.checkout.DeliveryOption
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen
import ru.livetyping.zarina.core.uikit.loader.ZarinaCircularLoader
import ru.livetyping.zarina.core.uikit.scroll.ZarinaScrollableDefaults
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.cart.ui.impl.R
import ru.livetyping.zarina.feature.cart.ui.impl.impl.deliveryaddressselector.model.DeliveryOptionsState

@Suppress("NAME_SHADOWING")
@Composable
internal fun DeliveryOptions(
    state: DeliveryOptionsState,
    onDeliveryOptionClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionDateClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionTimeClicked: (DeliveryOption) -> Unit,
    onDeliveryOptionShowDetailsClicked: (DeliveryOption) -> Unit,
    onErrorRefreshClicked: () -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is DeliveryOptionsState.Success -> ContentKey.Success
                DeliveryOptionsState.Loading -> it
                is DeliveryOptionsState.Error -> it
                DeliveryOptionsState.None -> it
            }
        },
        modifier = modifier,
    ) { state ->
        if (state != DeliveryOptionsState.None) {
            Column {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = stringResource(R.string.cart_choose_delivery_option),
                    style = UiKitTheme.typography.secondary.bold,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
                Spacer(modifier = Modifier.height(16.dp))

                when (state) {
                    is DeliveryOptionsState.Success -> {
                        DeliveryOptionsSuccess(
                            state = state,
                            onDeliveryOptionClicked = onDeliveryOptionClicked,
                            onDeliveryOptionDateClicked = { onDeliveryOptionDateClicked(it) },
                            onDeliveryOptionTimeClicked = { onDeliveryOptionTimeClicked(it) },
                            onDeliveryOptionShowDetailsClicked = onDeliveryOptionShowDetailsClicked,
                        )
                    }

                    DeliveryOptionsState.Loading -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(
                                    windowInsetsProvider().only(WindowInsetsSides.Bottom),
                                )
                                .padding(vertical = 32.dp),
                        ) {
                            ZarinaCircularLoader(modifier = Modifier.size(40.dp))
                        }
                    }

                    is DeliveryOptionsState.Error -> {
                        ZarinaErrorScreen(
                            state = state.state,
                            onButtonClicked = onErrorRefreshClicked,
                            modifier = Modifier
                                .windowInsetsPadding(
                                    windowInsetsProvider().only(WindowInsetsSides.Bottom),
                                )
                                .padding(horizontal = 16.dp, vertical = 32.dp),
                        )
                    }

                    DeliveryOptionsState.None -> Unit
                }

                Spacer(modifier = Modifier.windowInsetsBottomHeight(windowInsetsProvider()))
                Spacer(modifier = Modifier.height(ZarinaScrollableDefaults.ScrollableBottomPadding))
            }
        }
    }
}

private enum class ContentKey { Success }

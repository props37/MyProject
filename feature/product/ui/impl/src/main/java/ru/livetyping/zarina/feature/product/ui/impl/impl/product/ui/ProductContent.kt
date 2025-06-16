package ru.livetyping.zarina.feature.product.ui.impl.impl.product.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import ru.livetyping.zarina.core.uicompose.Crossfade
import ru.livetyping.zarina.core.uikit.error.ZarinaErrorScreen2
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.product.model.ProductState

@Suppress("NAME_SHADOWING")
@Composable
internal fun ProductContent(
    state: ProductState,
    onEvent: (ProductEvent) -> Unit,
    windowInsetsProvider: @Composable () -> WindowInsets,
    bottomPaddingProvider: @Composable () -> Dp,
    modifier: Modifier = Modifier,
) {
    Crossfade(
        targetState = state,
        contentKey = {
            when (it) {
                is ProductState.Success -> ProductContentKey.Success
                is ProductState.Error -> it
                ProductState.Loading -> it
            }
        },
        modifier = modifier,
    ) { state ->
        when (state) {
            is ProductState.Success -> {
                ProductSuccess(
                    state = state,
                    onEvent = onEvent,
                    windowInsetsProvider = windowInsetsProvider,
                    bottomPaddingProvider = bottomPaddingProvider,
                )
            }

            ProductState.Loading -> {
                ProductLoading(
                    windowInsetsProvider = windowInsetsProvider,
                    bottomPaddingProvider = bottomPaddingProvider,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is ProductState.Error -> {
                ZarinaErrorScreen2(
                    state = state.state,
                    onButtonClick = { onEvent(ProductEvent.ProductRefreshTriggered) },
                    bottomPaddingProvider = bottomPaddingProvider,
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(
                            windowInsetsProvider()
                                .only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
                        ),
                )
            }
        }
    }
}

private enum class ProductContentKey { Success }

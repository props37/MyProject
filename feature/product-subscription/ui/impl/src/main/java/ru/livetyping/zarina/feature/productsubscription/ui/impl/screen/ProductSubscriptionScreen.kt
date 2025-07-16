package ru.livetyping.zarina.feature.productsubscription.ui.impl.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottombar.navigation.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionFeature
import ru.livetyping.zarina.feature.productsubscription.ui.impl.screen.model.ProductSubscriptionEvent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.screen.model.ProductSubscriptionState
import ru.livetyping.zarina.feature.productsubscription.ui.impl.screen.ui.ProductSubscriptionContent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.screen.ui.TopBar

@Composable
internal fun ProductSubscriptionScreen(
    navActions: ProductSubscriptionFeature.NavActions,
    viewModel: ProductSubscriptionViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    state: ProductSubscriptionState,
    onEvent: (ProductSubscriptionEvent) -> Unit,
    sideEffects: Flow<ProductSubscriptionSideEffect>,
    navActions: ProductSubscriptionFeature.NavActions,
) {
    ProductSubscriptionScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            )
            .bottomNavBarPadding(),
    ) {
        TopBar(onBackClicked = { onEvent(ProductSubscriptionEvent.BackClicked) })

        ProductSubscriptionContent(
            state = state,
            onEvent = onEvent,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

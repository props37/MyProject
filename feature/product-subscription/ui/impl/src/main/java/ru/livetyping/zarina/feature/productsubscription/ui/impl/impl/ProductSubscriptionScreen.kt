package ru.livetyping.zarina.feature.productsubscription.ui.impl.impl

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
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.productsubscription.ui.api.ProductSubscriptionNavActions
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.component.ProductSubscriptionContent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.component.TopBar
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionEvent
import ru.livetyping.zarina.feature.productsubscription.ui.impl.impl.model.ProductSubscriptionState

@Composable
internal fun ProductSubscriptionScreen(
    navActions: ProductSubscriptionNavActions,
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
    navActions: ProductSubscriptionNavActions,
) {
    ProductSubscriptionScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
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

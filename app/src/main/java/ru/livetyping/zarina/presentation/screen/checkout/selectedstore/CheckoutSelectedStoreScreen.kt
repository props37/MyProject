package ru.livetyping.zarina.presentation.screen.checkout.selectedstore

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.domain.store.Store
import ru.livetyping.zarina.presentation.common.tooling.preview.ZarinaPreview
import ru.livetyping.zarina.presentation.screen.checkout.selectedstore.CheckoutSelectedStoreScreenComponents.TopBar
import ru.livetyping.zarina.presentation.screen.checkout.selectedstore.CheckoutSelectedStoreViewModel.SideEffect
import ru.livetyping.zarina.presentation.theme.UiKitTheme

@Composable
fun CheckoutSelectedStoreScreen(
    navigate: (CheckoutSelectedStoreScreenAction) -> Unit,
    viewModel: CheckoutSelectedStoreViewModel = hiltViewModel(),
) {
    val store by viewModel.store.collectAsStateWithLifecycle()

    ScreenContent(
        store = store,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navigate = navigate,
    )
}

@Composable
private fun ScreenContent(
    store: Store,
    onBackClicked: () -> Unit,
    sideEffects: Flow<SideEffect>,
    navigate: (CheckoutSelectedStoreScreenAction) -> Unit,
) {
    CheckoutSelectedStoreScreenBehavior(
        sideEffects = sideEffects,
        navigate = navigate,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default)
            .windowInsetsPadding(
                WindowInsets.statusBars
                    .union(WindowInsets.displayCutout),
            ),
    ) {
        TopBar(
            store = store,
            onBackClicked = onBackClicked,
        )
    }
}

@Preview
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun Preview() {
    ZarinaPreview {
        // TODO: [Low] Add preview
    }
}

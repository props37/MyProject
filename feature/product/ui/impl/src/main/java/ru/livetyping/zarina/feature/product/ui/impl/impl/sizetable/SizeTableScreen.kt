package ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.model.SizeTableEvent
import ru.livetyping.zarina.feature.product.ui.impl.impl.sizetable.ui.TopBar

@Composable
internal fun SizeTableScreen(
    navActions: SizeTableNavActions,
    viewModel: SizeTableViewModel = hiltViewModel(),
) {
    ScreenContent(
        onSizeTableEvent = viewModel::onSizeTableEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    onSizeTableEvent: (SizeTableEvent) -> Unit,
    sideEffects: Flow<SizeTableSideEffect>,
    navActions: SizeTableNavActions,
) {
    SizeTableScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme2.colors.white),
    ) {
        TopBar(
            onCloseClicked = { onSizeTableEvent(SizeTableEvent.CloseClicked) },
        )
    }
}

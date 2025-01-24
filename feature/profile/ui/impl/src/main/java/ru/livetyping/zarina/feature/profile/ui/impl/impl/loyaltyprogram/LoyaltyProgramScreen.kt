package ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomnavbar.bottomNavBarPadding
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.component.LoyaltyProgram
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.component.LoyaltyProgramTopBar
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model.LoyaltyProgramEvent
import ru.livetyping.zarina.feature.profile.ui.impl.impl.loyaltyprogram.model.LoyaltyProgramState

@Composable
internal fun LoyaltyProgramScreen(
    navActions: LoyaltyProgramNavActions,
    viewModel: LoyaltyProgramViewModel = hiltViewModel(),
) {
    val state by viewModel.loyaltyProgramState.collectAsStateWithLifecycle()

    ScreenContent(
        state = state,
        onEvent = viewModel::onLoyaltyProgramEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    state: LoyaltyProgramState,
    onEvent: (LoyaltyProgramEvent) -> Unit,
    sideEffects: Flow<LoyaltyProgramSideEffect>,
    navActions: LoyaltyProgramNavActions,
) {
    LoyaltyProgramScreenBehavior(
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
        LoyaltyProgramTopBar(onBackClicked = { onEvent(LoyaltyProgramEvent.BackClicked) })

        LoyaltyProgram(
            state = state,
            onEvent = onEvent,
        )
    }
}

package ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery

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
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.component.PasswordRecoveryContent
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.component.PasswordRecoveryTopBar
import ru.livetyping.zarina.feature.signin.ui.impl.impl.passwordrecovery.model.PasswordRecoveryState

@Composable
internal fun PasswordRecoveryScreen(
    navActions: PasswordRecoveryNavActions,
    viewModel: PasswordRecoveryViewModel = hiltViewModel(),
) {
    val passwordRecoveryState by viewModel.passwordRecoveryState.collectAsStateWithLifecycle()

    ScreenContent(
        passwordRecoveryState = passwordRecoveryState,
        onRequestPasswordRecoveryClicked = viewModel::onRequestPasswordRecoveryClicked,
        onBackClicked = viewModel::onBackClicked,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
internal fun ScreenContent(
    passwordRecoveryState: PasswordRecoveryState,
    onRequestPasswordRecoveryClicked: () -> Unit,
    onBackClicked: () -> Unit,
    sideEffects: Flow<PasswordRecoverySideEffect>,
    navActions: PasswordRecoveryNavActions,
) {
    PasswordRecoveryScreenBehavior(
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
        PasswordRecoveryTopBar(onBackClicked = onBackClicked)

        PasswordRecoveryContent(
            state = passwordRecoveryState,
            onRequestPasswordRecoveryClicked = onRequestPasswordRecoveryClicked,
            modifier = Modifier.fillMaxSize(),
        )
    }
}

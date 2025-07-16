package ru.livetyping.zarina.feature.signin.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.signin.ui.impl.passwordrecovery.PasswordRecoveryNavActions
import ru.livetyping.zarina.feature.signin.ui.impl.passwordrecovery.PasswordRecoveryNavEntry
import ru.livetyping.zarina.feature.signin.ui.impl.passwordrecovery.PasswordRecoveryScreen

internal fun NavGraphBuilder.passwordRecoveryScreen(actions: PasswordRecoveryNavActions) {
    composable<PasswordRecoveryNavEntry> {
        PasswordRecoveryScreen(actions)
    }
}

package ru.livetyping.zarina.feature.profile.ui.impl.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.BonusHistoryNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.BonusHistoryNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.impl.bonushistory.BonusHistoryScreen

internal fun NavGraphBuilder.bonusHistoryScreen(actions: BonusHistoryNavActions) {
    composable<BonusHistoryNavEntry> {
        BonusHistoryScreen(actions)
    }
}

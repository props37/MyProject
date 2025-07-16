package ru.livetyping.zarina.feature.profile.ui.impl.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramNavActions
import ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramNavEntry
import ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramScreen

internal fun NavGraphBuilder.loyaltyProgramScreen(actions: LoyaltyProgramNavActions) {
    composable<LoyaltyProgramNavEntry> {
        LoyaltyProgramScreen(actions)
    }
}

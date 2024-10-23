package ru.livetyping.zarina.feature.onboarding.ui.impl

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingFeatureEntry
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavEntry
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.OnboardingScreen

public class OnboardingFeatureEntryImpl : OnboardingFeatureEntry {
    override fun NavGraphBuilder.composable(actions: OnboardingNavActions) {
        composable<OnboardingNavEntry> {
            OnboardingScreen(navActions = actions)
        }
    }
}

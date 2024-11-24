package ru.livetyping.zarina.feature.onboarding.ui.impl.impl

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.Flow
import ru.livetyping.zarina.core.uikit.bottomsheet.ZarinaBottomSheet
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme
import ru.livetyping.zarina.feature.onboarding.ui.OnboardingNavActions
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.component.Banner
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.component.Onboarding
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.component.ProgressIndicator
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingEvent
import ru.livetyping.zarina.feature.onboarding.ui.impl.impl.model.OnboardingState

@Composable
internal fun OnboardingScreen(
    navActions: OnboardingNavActions,
    viewModel: OnboardingViewModel = hiltViewModel(),
) {
    val onboardingState by viewModel.onboardingState.collectAsStateWithLifecycle()

    ScreenContent(
        onboardingState = onboardingState,
        onOnboardingEvent = viewModel::onOnboardingEvent,
        sideEffects = viewModel.sideEffects,
        navActions = navActions,
    )
}

@Composable
private fun ScreenContent(
    onboardingState: OnboardingState,
    onOnboardingEvent: (OnboardingEvent) -> Unit,
    sideEffects: Flow<OnboardingSideEffect>,
    navActions: OnboardingNavActions,
) {
    OnboardingScreenBehavior(
        sideEffects = sideEffects,
        navActions = navActions,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(UiKitTheme.colors.background.general.regular.default),
    ) {
        Banner(
            url = onboardingState.bannerUrl,
            modifier = Modifier.fillMaxSize(),
        )

        ZarinaBottomSheet(modifier = Modifier.align(Alignment.BottomCenter)) {
            Column(
                modifier = Modifier
                    .windowInsetsPadding(
                        WindowInsets.systemBars
                            .union(WindowInsets.displayCutout)
                            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Bottom),
                    )
                    .padding(top = 24.dp, bottom = 16.dp),
            ) {
                ProgressIndicator(
                    onboardingState = onboardingState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Onboarding(
                    state = onboardingState,
                    onEvent = onOnboardingEvent,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

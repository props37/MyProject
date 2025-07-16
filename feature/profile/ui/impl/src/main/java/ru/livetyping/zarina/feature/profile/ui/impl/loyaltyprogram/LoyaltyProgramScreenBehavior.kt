package ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior

@Composable
internal fun LoyaltyProgramScreenBehavior(
    sideEffects: Flow<ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramSideEffect>,
    navActions: LoyaltyProgramNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)

    BottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ru.livetyping.zarina.feature.profile.ui.impl.loyaltyprogram.LoyaltyProgramSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: LoyaltyProgramNavActions, action: LoyaltyProgramScreenAction) {
    when (action) {
        LoyaltyProgramScreenAction.BackClicked -> navActions.onBackClicked()
        LoyaltyProgramScreenAction.BonusHistoryClicked -> navActions.onBonusHistoryClicked()
    }
}

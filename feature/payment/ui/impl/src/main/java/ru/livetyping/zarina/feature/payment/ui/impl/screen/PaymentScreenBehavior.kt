package ru.livetyping.zarina.feature.payment.ui.impl.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature

@Composable
internal fun PaymentScreenBehavior(
    sideEffects: Flow<PaymentSideEffect>,
    navActions: PaymentFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is PaymentSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is PaymentSideEffect.ShowZarinaToast -> {
                        currentZarinaToastController.show(sideEffect.message)
                    }
                }
            }
        }

        onStopOrDispose {
            job.cancel()
        }
    }
}

private fun navigate(navActions: PaymentFeature.NavActions, action: PaymentScreenAction) {
    when (action) {
        PaymentScreenAction.BackClicked -> navActions.onBackClicked()
    }
}

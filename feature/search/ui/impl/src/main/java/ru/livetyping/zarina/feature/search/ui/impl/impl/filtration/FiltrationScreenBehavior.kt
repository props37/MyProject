package ru.livetyping.zarina.feature.search.ui.impl.impl.filtration

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun FiltrationScreenBehavior(
    sideEffects: Flow<FiltrationSideEffect>,
    navActions: FiltrationNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is FiltrationSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is FiltrationSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: FiltrationNavActions, action: FiltrationScreenAction) {
    when (action) {
        FiltrationScreenAction.BackClicked -> navActions.onBackClicked()
        is FiltrationScreenAction.FilterClicked -> navActions.onFilterClicked(action.filter)
        is FiltrationScreenAction.ShowProductsClicked -> {
            navActions.onShowProductsClicked(action.filters)
        }
    }
}

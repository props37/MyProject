package ru.livetyping.zarina.feature.productlist.ui.impl.impl.listfilter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController

@Composable
internal fun ListFilterScreenBehavior(
    sideEffects: Flow<ListFilterSideEffect>,
    navActions: ListFilterNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is ListFilterSideEffect.Navigate -> {
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is ListFilterSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: ListFilterNavActions, action: ListFilterScreenAction) {
    when (action) {
        ListFilterScreenAction.BackClicked -> navActions.onBackClicked()
        is ListFilterScreenAction.AppliedClicked -> navActions.onApplyClicked(action.filter)
    }
}

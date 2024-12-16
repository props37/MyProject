package ru.livetyping.zarina.feature.cityselector.ui.impl.impl

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uicomponent.bottomnavbar.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorNavActions

@Composable
internal fun CitySelectorScreenBehavior(
    sideEffects: Flow<CitySelectorSideEffect>,
    navActions: CitySelectorNavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CitySelectorSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
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

private fun navigate(navActions: CitySelectorNavActions, action: CitySelectorScreenAction) {
    when (action) {
        CitySelectorScreenAction.BackClicked -> navActions.onBackClicked()
        is CitySelectorScreenAction.CitySelected -> navActions.onCitySelected(action.city)
    }
}

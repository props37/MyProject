package ru.livetyping.zarina.feature.cityselector.ui.impl.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.core.uikit.bottombar.navigation.behavior.BottomNavBarBehavior
import ru.livetyping.zarina.core.uikit.toast.LocalZarinaToastController2
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature

@Composable
internal fun CitySelectorScreenBehavior(
    sideEffects: Flow<CitySelectorSideEffect>,
    navActions: CitySelectorFeature.NavActions,
) {
    val currentNavActions by rememberUpdatedState(navActions)
    val currentKeyboardController by rememberUpdatedState(LocalSoftwareKeyboardController.current)
    val currentZarinaToastController by rememberUpdatedState(LocalZarinaToastController2.current)

    BottomNavBarBehavior(isVisible = false)

    LifecycleStartEffect(sideEffects) {
        val job = lifecycleScope.launch {
            sideEffects.collect { sideEffect ->
                when (sideEffect) {
                    is CitySelectorSideEffect.Navigate -> {
                        currentKeyboardController?.hide()
                        navigate(currentNavActions, sideEffect.action)
                    }

                    is CitySelectorSideEffect.ShowZarinaToast -> {
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

private fun navigate(navActions: CitySelectorFeature.NavActions, action: CitySelectorScreenAction) {
    when (action) {
        CitySelectorScreenAction.BackClicked -> navActions.onBackClicked()
        is CitySelectorScreenAction.CitySelected -> navActions.onCitySelected(action.city)
    }
}

package ru.livetyping.zarina.presentation.screen.shops

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import ru.livetyping.zarina.presentation.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.livetyping.zarina.presentation.common.systemsettings.openSettings
import ru.livetyping.zarina.presentation.common.zarinasnack.controller.LocalZarinaSnackController
import ru.livetyping.zarina.presentation.common.zarinatoast.controller.LocalZarinaToastController
import ru.livetyping.zarina.presentation.screen.shops.ShopsViewModel.SideEffect

@Composable
fun ShopsScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigate: (ShopsScreenAction) -> Unit,
) {
    val updatedContext by rememberUpdatedState(LocalContext.current)
    val updatedZarinaToastController by rememberUpdatedState(LocalZarinaToastController.current)
    val updatedZarinaSnackController by rememberUpdatedState(LocalZarinaSnackController.current)
    val updatedNavigate by rememberUpdatedState(navigate)

    ForcedBottomNavBarBehavior(isVisible = true)

    LifecycleStartEffect(sideEffects) {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                sideEffects.collect { sideEffect ->
                    when (sideEffect) {
                        is SideEffect.Navigate -> updatedNavigate(sideEffect.action)
                        is SideEffect.ShowZarinaToast -> {
                            updatedZarinaToastController.show(sideEffect.message)
                        }

                        is SideEffect.ShowZarinaSnack -> {
                            updatedZarinaSnackController.show(sideEffect.message)
                        }

                        is SideEffect.OpenSettings -> {
                            updatedContext.openSettings(sideEffect.settings)
                        }
                    }
                }
            }
        }

        onStopOrDispose {}
    }
}

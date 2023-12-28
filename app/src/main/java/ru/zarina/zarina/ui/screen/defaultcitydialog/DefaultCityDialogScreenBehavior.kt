package ru.zarina.zarina.ui.screen.defaultcitydialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.screen.defaultcitydialog.DefaultCityDialogViewModel.SideEffect

@Composable
fun DefaultCityDialogScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (DefaultCityDialogScreenResult) -> Unit,
) {
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
            }
        }
    }
}

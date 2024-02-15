package ru.zarina.zarina.ui.screen.sizeselector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.screen.sizeselector.SizeSelectorViewModel.SideEffect

@Composable
fun SizeSelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (SizeSelectorScreenAction) -> Unit,
    navigateBackward: (SizeSelectorScreenResult) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateForward -> updatedNavigateForward(sideEffect.action)
                is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
            }
        }
    }
}

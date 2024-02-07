package ru.zarina.zarina.ui.screen.sizetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.screen.sizetable.SizeSelectorViewModel.SideEffect

@Composable
fun SizeSelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (SizeSelectorScreenResult) -> Unit,
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

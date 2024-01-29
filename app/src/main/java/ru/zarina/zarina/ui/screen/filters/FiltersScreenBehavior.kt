package ru.zarina.zarina.ui.screen.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.filters.FiltersViewModel.SideEffect

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (FiltersScreenAction) -> Unit,
    navigateBackward: (FiltersScreenResult) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = false)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateForward -> updatedNavigateForward(sideEffect.action)
                is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
            }
        }
    }
}

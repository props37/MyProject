package ru.zarina.zarina.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.home.HomeViewModel.SideEffect

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateForward: (HomeScreenAction) -> Unit,
) {
    val updatedNavigateForward by rememberUpdatedState(navigateForward)

    ForcedBottomNavBarBehavior(isVisible = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateForward -> updatedNavigateForward(sideEffect.action)
            }
        }
    }
}


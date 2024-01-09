package ru.zarina.zarina.ui.screen.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.home.HomeViewModel.SideEffect

@Composable
fun HomeScreenBehavior(
    sideEffects: Flow<SideEffect>,
) {
    ForcedBottomNavBarBehavior(isVisible = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->

        }
    }
}


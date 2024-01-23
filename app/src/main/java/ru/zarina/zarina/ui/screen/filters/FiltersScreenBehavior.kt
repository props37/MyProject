package ru.zarina.zarina.ui.screen.filters

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior

@Composable
fun FiltersScreenBehavior(
    sideEffects: Flow<FiltersViewModel.SideEffect>,
) {
    ForcedBottomNavBarBehavior(isVisible = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->

        }
    }
}

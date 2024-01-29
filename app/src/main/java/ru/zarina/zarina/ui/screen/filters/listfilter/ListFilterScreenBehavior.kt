package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterViewModel.SideEffect

@Composable
fun ListFilterScreenBehavior(
    sideEffects: Flow<SideEffect>,
) {
    ForcedBottomNavBarBehavior(isVisible = false)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->

        }
    }
}

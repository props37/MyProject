package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.filters.listfilter.ListFilterViewModel.SideEffect

@Composable
fun ListFilterScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: (ListFilterScreenResult) -> Unit,
) {
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = false)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                is SideEffect.NavigateBackward -> updatedNavigateBackward(sideEffect.result)
            }
        }
    }
}

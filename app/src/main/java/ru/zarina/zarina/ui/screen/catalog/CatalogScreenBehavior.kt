package ru.zarina.zarina.ui.screen.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalFocusManager
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.catalog.CatalogViewModel.SideEffect

@Composable
fun CatalogScreenBehavior(
    sideEffects: Flow<SideEffect>,
) {
    val updatedFocusManager by rememberUpdatedState(LocalFocusManager.current)

    ForcedBottomNavBarBehavior(isVisible = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                SideEffect.FreeSearchBarFocus -> updatedFocusManager.clearFocus(force = true)
            }
        }
    }
}


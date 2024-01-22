package ru.zarina.zarina.ui.screen.products

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.common.behavior.bottomnavbar.ForcedBottomNavBarBehavior
import ru.zarina.zarina.ui.screen.products.ProductsViewModel.SideEffect

@Composable
fun ProductsScreenBehavior(
    sideEffects: Flow<SideEffect>,
    navigateBackward: () -> Unit,
) {
    val updatedNavigateBackward by rememberUpdatedState(navigateBackward)

    ForcedBottomNavBarBehavior(isVisible = true)

    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->
            when (sideEffect) {
                SideEffect.NavigateBackward -> updatedNavigateBackward()
            }
        }
    }
}

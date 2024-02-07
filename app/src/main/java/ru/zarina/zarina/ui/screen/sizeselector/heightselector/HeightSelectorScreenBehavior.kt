package ru.zarina.zarina.ui.screen.sizeselector.heightselector

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow
import ru.zarina.zarina.ui.screen.sizeselector.heightselector.HeightSelectorViewModel.SideEffect

@Composable
fun HeightSelectorScreenBehavior(
    sideEffects: Flow<SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->

        }
    }
}

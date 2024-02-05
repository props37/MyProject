package ru.zarina.zarina.ui.screen.sizetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.flow.Flow

@Composable
fun SizeTableScreenBehavior(
    sideEffects: Flow<SizeTableViewModel.SideEffect>,
) {
    LaunchedEffect(sideEffects) {
        sideEffects.collect { sideEffect ->

        }
    }
}

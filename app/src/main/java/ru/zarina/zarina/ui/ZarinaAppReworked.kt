package ru.zarina.zarina.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.zarina.zarina.ui.navigation.rework.ZarinaNavigationReworked

@Composable
fun ZarinaAppReworked(
    modifier: Modifier = Modifier,
) {
    ZarinaNavigationReworked(modifier = modifier)
}

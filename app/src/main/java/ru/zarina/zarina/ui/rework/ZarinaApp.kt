package ru.zarina.zarina.ui.rework

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.zarina.zarina.ui.navigation.rework.ZarinaNavigation

@Composable
fun ZarinaApp(
    modifier: Modifier = Modifier,
) {
    ZarinaNavigation(modifier = modifier)
}

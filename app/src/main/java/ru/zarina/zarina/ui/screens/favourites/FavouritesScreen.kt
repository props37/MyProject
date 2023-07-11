package ru.zarina.zarina.ui.screens.favourites

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.behavior.navigationbar.NavigationBarState
import ru.zarina.zarina.ui.common.components.ScreenPlaceholder

@Composable
fun FavouritesScreen() {
    NavigationBarState(isVisible = true, isAnimated = true)
    ScreenPlaceholder(title = stringResource(id = R.string.favourites))
}

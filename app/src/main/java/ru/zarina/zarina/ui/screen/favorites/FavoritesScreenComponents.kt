package ru.zarina.zarina.ui.screen.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.base.rememberErrorState
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.animation.AnimatedContentDefaultExitTransition

object FavoritesScreenComponents {

    @Composable
    fun TopBar(
        isClearButtonVisible: Boolean,
        onClearClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.favorites),
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isClearButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = onClearClicked,
                        size = ZarinaButtonSize.Small,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        textStyle = UiKitTheme.typography.caption1.regular,
                    ) {
                        Text(text = stringResource(R.string.clear).uppercase())
                    }
                }
            },
            modifier = modifier,
        )
    }

    @Composable
    fun FavoriteProductsNotFoundPlaceholder(
        onGoToCatalogClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        val state = rememberErrorState(
            iconResId = R.drawable.ic_heart_outline_64,
            title = stringResource(R.string.favorites_screen_no_favorites_placeholder_title),
            body = stringResource(R.string.favorites_screen_no_favorites_placeholder_body),
            buttonText = stringResource(R.string.go_to_catalog)
        )

        ZarinaErrorScreen(
            state = state,
            onButtonClicked = onGoToCatalogClicked,
            modifier = modifier,
        )
    }
}

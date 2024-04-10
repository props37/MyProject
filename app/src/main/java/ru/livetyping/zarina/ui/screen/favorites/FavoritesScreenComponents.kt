package ru.livetyping.zarina.ui.screen.favorites

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.component.button.ZarinaButton
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.livetyping.zarina.ui.common.component.button.ZarinaButtonSize
import ru.livetyping.zarina.ui.common.component.screen.ZarinaErrorScreen
import ru.livetyping.zarina.ui.common.component.topbar.TopBarDefaults
import ru.livetyping.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.livetyping.zarina.ui.common.error.rememberErrorState
import ru.livetyping.zarina.ui.theme.UiKitTheme
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultEnterTransition
import ru.livetyping.zarina.util.compose.animation.AnimatedContentDefaultExitTransition

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
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
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
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(text = stringResource(R.string.clear).uppercase())
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
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
            title = stringResource(R.string.favorites_no_favorites_placeholder_title),
            body = stringResource(R.string.favorites_no_favorites_placeholder_body),
            buttonText = stringResource(R.string.go_to_catalog)
        )

        ZarinaErrorScreen(
            state = state,
            onButtonClicked = onGoToCatalogClicked,
            modifier = modifier,
        )
    }
}

package ru.zarina.zarina.ui.screen.filters.listfilter

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme
import ru.zarina.zarina.util.compose.AnimatedContentDefaultEnterTransition
import ru.zarina.zarina.util.compose.AnimatedContentDefaultExitTransition

object ListFilterScreenComponents {

    @Composable
    fun TopBar(
        title: String,
        isResetButtonVisible: Boolean,
        actions: TopBarActions,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                BackIconButton(
                    onClick = actions.onBackClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = title,
                    style = UiKitTheme.typographyReworked.primary.regular,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            },
            endContent = {
                AnimatedVisibility(
                    visible = isResetButtonVisible,
                    enter = AnimatedContentDefaultEnterTransition,
                    exit = AnimatedContentDefaultExitTransition,
                ) {
                    ZarinaButton(
                        onClick = actions.onResetClicked,
                        size = ZarinaButtonSize.Small,
                        colors = ZarinaButtonDefaults.backlessColors(),
                        modifier = Modifier.padding(end = 8.dp),
                    ) {
                        Text(
                            text = stringResource(R.string.reset).uppercase(),
                            style = UiKitTheme.typographyReworked.caption1.regular,
                        )
                    }
                }
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    @Stable
    class TopBarActions(
        val onBackClicked: () -> Unit,
        val onResetClicked: () -> Unit,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as TopBarActions

            if (onBackClicked != other.onBackClicked) return false
            return onResetClicked == other.onResetClicked
        }

        override fun hashCode(): Int {
            var result = onBackClicked.hashCode()
            result = 31 * result + onResetClicked.hashCode()
            return result
        }
    }
}

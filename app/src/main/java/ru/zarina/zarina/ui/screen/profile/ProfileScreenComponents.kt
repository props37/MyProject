package ru.zarina.zarina.ui.screen.profile

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme

object ProfileScreenComponents {

    @Composable
    fun TopBar(
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.profile),
                    style = UiKitTheme.typography.primary.regular,
                    color = UiKitTheme.colors.text.general.regular.default,
                )
            },
            modifier = modifier,
        )
    }
}

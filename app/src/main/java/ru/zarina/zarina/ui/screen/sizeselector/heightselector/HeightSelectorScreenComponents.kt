package ru.zarina.zarina.ui.screen.sizeselector.heightselector

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.BackIconButton
import ru.zarina.zarina.ui.common.component.button.CloseIconButton
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme

object HeightSelectorScreenComponents {

    @Composable
    fun TopBar(
        onBackClicked: () -> Unit,
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            startContent = {
                BackIconButton(
                    onClick = onBackClicked,
                    iconSize = TopBarIconSize,
                    modifier = Modifier.padding(start = 2.dp),
                )
            },
            centerContent = {
                Text(
                    text = stringResource(R.string.choose_height),
                    style = UiKitTheme.typographyReworked.primary.bold,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            },
            endContent = {
                CloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = TopBarIconSize,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }

    private val TopBarIconSize: Dp get() = 20.dp
}

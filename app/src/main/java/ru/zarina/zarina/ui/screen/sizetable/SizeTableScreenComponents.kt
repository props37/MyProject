package ru.zarina.zarina.ui.screen.sizetable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.component.button.CloseIconButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButton
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonDefaults
import ru.zarina.zarina.ui.common.component.button.ZarinaButtonSize
import ru.zarina.zarina.ui.common.component.topbar.TopBarDefaults
import ru.zarina.zarina.ui.common.component.topbar.ZarinaTopBar
import ru.zarina.zarina.ui.theme.UiKitTheme

object SizeTableScreenComponents {

    @Composable
    fun SizeTableLabel(modifier: Modifier = Modifier) {
        ZarinaButton(
            onClick = {},
            isEnabled = false,
            size = ZarinaButtonSize.Medium,
            colors = ZarinaButtonDefaults.secondaryColors(
                disabledBackgroundColor = UiKitTheme.colorsReworked.background.button.secondary.default,
                disabledContentColor = UiKitTheme.colorsReworked.text.button.secondary.default,
            ),
            modifier = modifier,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_ruler_24),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.size_table).uppercase(),
                modifier = Modifier.padding(top = 2.dp), // Circe font padding
            )
        }
    }

    @Composable
    fun TopBar(
        onCloseClicked: () -> Unit,
        modifier: Modifier = Modifier,
    ) {
        ZarinaTopBar(
            centerContent = {
                Text(
                    text = stringResource(R.string.choose_size),
                    style = UiKitTheme.typographyReworked.primary.bold,
                    color = UiKitTheme.colorsReworked.text.general.regular.default,
                )
            },
            endContent = {
                CloseIconButton(
                    onClick = onCloseClicked,
                    iconSize = 20.dp,
                    modifier = Modifier.padding(end = 2.dp),
                )
            },
            contentPadding = PaddingValues(vertical = TopBarDefaults.VerticalPadding),
            modifier = modifier,
        )
    }
}

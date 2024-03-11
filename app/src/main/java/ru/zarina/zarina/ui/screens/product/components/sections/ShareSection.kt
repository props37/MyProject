package ru.zarina.zarina.ui.screens.product.components.sections

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButton
import ru.zarina.zarina.ui.common.components.buttons.ZarinaButtonDefaults
import ru.zarina.zarina.ui.theme.old.UiKitTheme

@Composable
fun ShareSection(
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaButton(
        onClick = onShareClick,
        colors = ZarinaButtonDefaults.secondaryColors(),
        modifier = modifier,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_share_24),
                contentDescription = null,
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = stringResource(id = R.string.share_product),
                style = UiKitTheme.typography.circle1720bold,
                color = UiKitTheme.colors.secondaryButtonForeground,
                maxLines = 1,
            )
        }
    }
}

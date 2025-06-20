package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickuppoint.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaBackIconButton
import ru.livetyping.zarina.core.uikit.theme.UiKitTheme2
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cart.ui.impl.R

@Composable
internal fun TopBar(
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        startContent = {
            ZarinaBackIconButton(onClick = onBackClicked)
        },
        centerContent = {
            Text(
                text = stringResource(R.string.cart_pickup_point_info).uppercase(),
                style = UiKitTheme2.typography.body,
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}

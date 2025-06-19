package ru.livetyping.zarina.feature.cart.ui.impl.impl.orderconfirmed.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar

@Composable
internal fun TopBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        endContent = {
            ZarinaCloseIconButton(onClick = onCloseClicked)
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}

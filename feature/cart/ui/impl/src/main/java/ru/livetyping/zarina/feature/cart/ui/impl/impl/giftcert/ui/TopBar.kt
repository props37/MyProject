package ru.livetyping.zarina.feature.cart.ui.impl.impl.giftcert.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun TopBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        centerContent = {
            Text(text = stringResource(RCommon.string.res_gift_certificate))
        },
        endContent = {
            ZarinaCloseIconButton(
                onClick = onCloseClicked,
                iconSize = 20.dp,
                modifier = Modifier.padding(end = 2.dp),
            )
        },
        contentPadding = PaddingValues(vertical = 4.dp),
        modifier = modifier,
    )
}

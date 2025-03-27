package ru.livetyping.zarina.feature.cart.ui.impl.impl.selectedpickupstore.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaButton
import ru.livetyping.zarina.core.uikit.divider.ZarinaDivider
import ru.livetyping.zarina.core.resource.R as RCommon

@Composable
internal fun ContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    windowInsetsProvider: @Composable () -> WindowInsets = { WindowInsets.safeDrawing },
) {
    Column(modifier = modifier) {
        ZarinaDivider(modifier = Modifier.fillMaxWidth())

        ZarinaButton(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 20.dp)
                .windowInsetsPadding(windowInsetsProvider().only(WindowInsetsSides.Bottom)),
        ) {
            Text(text = stringResource(RCommon.string.res_select).uppercase())
        }
    }
}

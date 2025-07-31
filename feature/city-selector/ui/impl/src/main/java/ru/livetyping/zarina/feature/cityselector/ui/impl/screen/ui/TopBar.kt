package ru.livetyping.zarina.feature.cityselector.ui.impl.screen.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.livetyping.zarina.core.uikit.button.ZarinaCloseIconButton
import ru.livetyping.zarina.core.uikit.topbar.ZarinaTopBar
import ru.livetyping.zarina.feature.cityselector.ui.impl.R

@Composable
internal fun TopBar(
    onCloseClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ZarinaTopBar(
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 8.dp,
            bottom = 4.dp,
        ),
        modifier = modifier,
    ) {
        Text(
            text = stringResource(R.string.city_selector_select_your_city).uppercase(),
            modifier = Modifier.weight(1f),
        )

        ZarinaCloseIconButton(onClick = onCloseClicked)
    }
}

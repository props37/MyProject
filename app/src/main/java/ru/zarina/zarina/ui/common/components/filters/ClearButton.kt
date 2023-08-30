package ru.zarina.zarina.ui.common.components.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.toolbar.TextButton

@Composable
fun ClearButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        text = stringResource(id = R.string.reset),
        onClick = onClick,
        modifier = modifier
    )
}

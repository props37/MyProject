package ru.livetyping.zarina.ui.common.components.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.R
import ru.livetyping.zarina.ui.common.components.FilterButtonMode
import ru.livetyping.zarina.ui.common.components.buttons.ZarinaTextButton

@Composable
fun FilterButton(
    mode: FilterButtonMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = stringResource(
        when (mode) {
            FilterButtonMode.APPLY -> R.string.apply
            FilterButtonMode.CLOSE -> R.string.close
        }
    )
    ZarinaTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier
    )
}

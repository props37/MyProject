package ru.zarina.zarina.ui.common.components.filters

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.zarina.zarina.R
import ru.zarina.zarina.ui.common.components.buttons.ZarinaTextButton
import ru.zarina.zarina.ui.screens.catalog.filters.FiltersViewModel

@Composable
fun FilterButton(
    mode: FiltersViewModel.FilterButtonMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = stringResource(
        when (mode) {
            FiltersViewModel.FilterButtonMode.APPLY -> R.string.apply
            FiltersViewModel.FilterButtonMode.CLOSE -> R.string.close
        }
    )
    ZarinaTextButton(
        text = text,
        onClick = onClick,
        modifier = modifier
    )
}

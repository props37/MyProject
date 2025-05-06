package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField

// TODO: [Top] Update according design

@Composable
internal fun SearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    ZarinaTextField(
        state = state,
        modifier = modifier,
    )
}

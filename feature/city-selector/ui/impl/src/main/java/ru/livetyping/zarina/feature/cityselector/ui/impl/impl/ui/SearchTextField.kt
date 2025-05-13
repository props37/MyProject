package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.ui

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.livetyping.zarina.core.uikit.text.ZarinaTextField
import ru.livetyping.zarina.feature.cityselector.ui.impl.R

@Composable
internal fun SearchTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
) {
    ZarinaTextField(
        state = state,
        placeholder = {
            Text(text = stringResource(R.string.city_selector_city_name).uppercase())
        },
        modifier = modifier,
    )
}

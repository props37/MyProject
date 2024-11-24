package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.text.Text

@Stable
internal data class TopBarState(
    val title: Text,
    val citySearchTextFieldState: TextFieldState,
)

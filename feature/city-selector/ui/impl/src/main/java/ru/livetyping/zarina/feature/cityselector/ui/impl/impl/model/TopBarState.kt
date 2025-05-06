package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class TopBarState(
    val citySearchTextFieldState: TextFieldState,
)

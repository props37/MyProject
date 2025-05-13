package ru.livetyping.zarina.feature.cityselector.ui.impl.impl.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class CitySelectorState(
    val citySearchTextFieldState: TextFieldState,
    val cityListState: CityListState,
)

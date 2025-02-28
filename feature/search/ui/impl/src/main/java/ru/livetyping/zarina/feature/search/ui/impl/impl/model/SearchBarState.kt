package ru.livetyping.zarina.feature.search.ui.impl.impl.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable

@Stable
internal data class SearchBarState(
    val textFieldState: TextFieldState,
    val searchMode: SearchMode,
    val appliedFilterCount: Int,
)

package ru.livetyping.zarina.core.uicomponent.filtration.listfilter.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.text.Text

@Immutable
public data class ListFilterTopBarState(
    val title: Text,
    val isResetFilterButtonVisible: Boolean,
)

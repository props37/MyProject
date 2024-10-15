package ru.livetyping.zarina.feature.home.ui.impl.impl.gender

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
internal sealed interface GenderSelectorEvent {

    @Immutable
    data class GenderChanged(val gender: GenderTab) : GenderSelectorEvent
}

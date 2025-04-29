package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed class MenuState {
    @Immutable
    data class Success(val items: ImmutableList<MenuItem>) : MenuState()

    data object Loading : MenuState()

    // TODO: [Top] Implement when design is ready
    data object Error : MenuState()
}

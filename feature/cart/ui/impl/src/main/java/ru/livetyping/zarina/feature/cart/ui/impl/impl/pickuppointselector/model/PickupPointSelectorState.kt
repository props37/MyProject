package ru.livetyping.zarina.feature.cart.ui.impl.impl.pickuppointselector.model

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList
import ru.livetyping.zarina.core.uimodel.tab.TabRowState

@Stable
internal data class PickupPointSelectorState(
    val filterTextFieldState: TextFieldState,
    val filters: ImmutableList<ToggleableFilter>,
    val viewModeSelectorState: TabRowState<ViewMode>,
)

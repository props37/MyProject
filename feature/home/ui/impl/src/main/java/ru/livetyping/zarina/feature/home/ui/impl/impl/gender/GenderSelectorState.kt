package ru.livetyping.zarina.feature.home.ui.impl.impl.gender

import androidx.compose.runtime.Immutable
import kotlinx.collections.immutable.ImmutableList

// TODO: [High] Consider moving to :core:ui-* module
@Immutable
internal data class GenderSelectorState(
    val genders: ImmutableList<GenderTab>,
    val currentGender: GenderTab,
)

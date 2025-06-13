package ru.livetyping.zarina.feature.product.ui.impl.impl.product.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed class SizeSelectorState {
    @Immutable
    data class Visible(
        val type: SizeSelectorType,
        val items: ImmutableList<SizeSelectorItem>,
    ) : SizeSelectorState()

    data object Hidden : SizeSelectorState()
}

internal enum class SizeSelectorType { SIZE, HEIGHT }

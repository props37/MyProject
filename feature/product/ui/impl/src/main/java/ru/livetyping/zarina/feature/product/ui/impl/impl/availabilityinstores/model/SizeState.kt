package ru.livetyping.zarina.feature.product.ui.impl.impl.availabilityinstores.model

import androidx.compose.runtime.Stable
import kotlinx.collections.immutable.ImmutableList

@Stable
internal sealed class SizeState {
    data class Success(val sizes: ImmutableList<Size>) : SizeState()

    data object Empty : SizeState()
}

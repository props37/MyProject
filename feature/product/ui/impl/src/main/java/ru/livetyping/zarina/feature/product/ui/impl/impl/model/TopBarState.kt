package ru.livetyping.zarina.feature.product.ui.impl.impl.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class TopBarState(
    val productName: String?,
)

internal enum class TopBarMode { Transparent, Filled }

package ru.livetyping.zarina.feature.productlist.ui.impl.impl.model

import androidx.compose.runtime.Immutable

@Immutable
internal data class TopBarState(
    val categoryName: String?,
    val appliedFilterCount: Int,
)

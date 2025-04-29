package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem

@Immutable
internal data class MenuItem(
    val item: CatalogMenuItem,
    val isExpanded: Boolean,
    val addBrackets: Boolean,
    val addStartPadding: Boolean,
)

package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem

@Stable
internal sealed class MenuItem {
    @Immutable
    data class Generic(
        val item: CatalogMenuItem,
        val isExpanded: Boolean,
        val addBrackets: Boolean,
        val addStartPadding: Boolean,
    ) : MenuItem()
}

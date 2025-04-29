package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem

@Stable
internal sealed class MenuItem {
    abstract val id: String

    @Immutable
    data class Basic(
        val item: CatalogMenuItem,
        val isExpanded: Boolean,
        val addBrackets: Boolean,
        val addStartPadding: Boolean,
        val isHighlighted: Boolean,
    ) : MenuItem() {
        override val id: String get() = item.id.value
    }

    @Immutable
    data class Spacer(
        override val id: String,
    ) : MenuItem()
}

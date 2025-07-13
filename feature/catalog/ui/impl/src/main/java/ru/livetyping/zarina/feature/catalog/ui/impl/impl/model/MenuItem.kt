package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.annotation.IntRange
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.catalog.CatalogMenuItem
import ru.livetyping.zarina.core.domain.model.geo.City as DomainCity

@Stable
internal sealed class MenuItem {
    abstract val id: String

    @Immutable
    data class Basic(
        val item: CatalogMenuItem,
        val isExpanded: Boolean,
        val addBrackets: Boolean,
        @IntRange(from = 0L)
        val nestingLevel: Int,
        val isHighlighted: Boolean,
    ) : MenuItem() {
        override val id: String get() = item.id.value
    }

    data object FeedbackWidget : MenuItem() {
        override val id: String get() = toString()
    }

    @Immutable
    data class City(val city: DomainCity) : MenuItem() {
        override val id: String get() = "Current City"
    }

    data object SupportPhoneNumber : MenuItem() {
        override val id: String get() = toString()
    }

    data object SupportEmailAddress : MenuItem() {
        override val id: String get() = toString()
    }

    @Immutable
    data class Spacer(
        override val id: String,
        val size: Size,
    ) : MenuItem() {
        enum class Size { SMALL, MEDIUM }
    }

    companion object {
        const val INITIAL_NESTING_LEVEL = 0
    }
}

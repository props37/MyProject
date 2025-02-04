package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.category.Category

@Stable
internal sealed class CategoryListItem {
    abstract val id: Id
    abstract val nestingLevel: Int

    @Immutable
    data class CategoryItem(
        val category: Category,
        override val nestingLevel: Int,
        val isExpandable: Boolean,
    ) : CategoryListItem() {
        override val id = Id(category.id.value)
    }

    @Immutable
    data class SeeWholeCategoryItem(
        val category: Category,
        override val nestingLevel: Int,
    ) : CategoryListItem() {
        override val id = Id("$ID_PREFIX${category.id.value}")

        companion object {
            private const val ID_PREFIX = "see_whole_category"
        }
    }

    @JvmInline
    value class Id(val value: String)

    companion object {
        const val NESTING_LEVEL_MIN_VALUE = 0

        fun fromCategory(category: Category, nestingLevel: Int): CategoryItem {
            return CategoryItem(
                category = category,
                nestingLevel = nestingLevel,
                isExpandable = category.isExpandable && !category.children.isNullOrEmpty(),
            )
        }
    }
}

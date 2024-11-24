package ru.livetyping.zarina.feature.catalog.ui.impl.impl.model

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import ru.livetyping.zarina.core.domain.model.category.Category

@Stable
internal sealed class CategoryListItem(
    open val id: Id,
    open val nestingLevel: Int,
) {
    init {
        checkNestingLevel()
    }

    private fun checkNestingLevel() {
        check(nestingLevel >= NESTING_LEVEL_MIN_VALUE) {
            "nestingLevel $nestingLevel must be at least $NESTING_LEVEL_MIN_VALUE"
        }
    }

    @Immutable
    data class CategoryItem(
        val category: Category,
        override val nestingLevel: Int,
        val isExpandable: Boolean,
    ) : CategoryListItem(createId(category), nestingLevel) {
        private companion object {
            private fun createId(category: Category): Id {
                return Id(category.id.value)
            }
        }
    }

    @Immutable
    data class SeeWholeCategoryItem(
        val category: Category,
        override val nestingLevel: Int,
    ) : CategoryListItem(createId(category), nestingLevel) {
        companion object {
            private const val ID_PREFIX = "see_whole_category"

            private fun createId(category: Category): Id {
                return Id("$ID_PREFIX${category.id.value}")
            }
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

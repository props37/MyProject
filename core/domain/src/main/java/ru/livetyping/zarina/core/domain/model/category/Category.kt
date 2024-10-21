package ru.livetyping.zarina.core.domain.model.category

import ru.livetyping.zarina.core.domain.model.common.Color

public data class Category(
    val id: Id,
    val name: String,
    val label: String?,
    val color: Color?,
    val isExpandable: Boolean,
    val children: List<Category>?,
) {
    @JvmInline
    public value class Id(public val value: String)
}

public fun Category.withFlattenedChildren(): List<Category> {
    val category = this
    return buildList {
        add(category)
        val flattenedChildren = category.getFlattenedChildren()
        if (flattenedChildren != null) {
            addAll(flattenedChildren)
        }
    }
}

public fun Category.getFlattenedChildren(): List<Category>? {
    return this.children?.let { categories ->
        buildList {
            addAll(categories)
            val flattenedChildren = categories.flatMap {
                it.getFlattenedChildren() ?: emptyList()
            }
            addAll(flattenedChildren)
        }
    }
}

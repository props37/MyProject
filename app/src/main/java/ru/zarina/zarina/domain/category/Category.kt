package ru.zarina.zarina.domain.category

import ru.zarina.zarina.domain.rework.common.Color

data class Category(
    val id: Id,
    val name: String,
    val label: String?,
    val color: Color?,
    val isExpandable: Boolean,
    val children: List<Category>?,
) {
    @JvmInline
    value class Id(val value: Long)
}

fun Category.withFlattenedChildren(): List<Category> {
    val category = this
    return buildList {
        add(category)
        val flattenedChildren = category.getFlattenedChildren()
        if (flattenedChildren != null) {
            addAll(flattenedChildren)
        }
    }
}

fun Category.getFlattenedChildren(): List<Category>? {
    return this.children?.let { categories ->
        buildList {
            addAll(categories)
            val flattenedChildren = categories.flatMap { it.getFlattenedChildren() ?: emptyList() }
            addAll(flattenedChildren)
        }
    }
}

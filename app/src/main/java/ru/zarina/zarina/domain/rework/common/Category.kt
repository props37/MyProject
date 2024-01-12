package ru.zarina.zarina.domain.rework.common

data class Category(
    val id: Id,
    val code: Code,
    val name: String,
    val label: String?,
    val color: Color?,
    val children: List<Category>?,
) {
    @JvmInline
    value class Id(val value: Long)

    @JvmInline
    value class Code(val value: String)

    companion object {
        val WOMEN_MAIN_CATEGORY_ID: Id get() = Id(1460)
        val MEN_MAIN_CATEGORY_ID: Id get() = Id(1461)
    }
}

fun Category.getFlattenedChildren(): List<Category>? {
    return children?.let {
        children + children.flatMap { it.getFlattenedChildren() ?: emptyList() }
    }
}

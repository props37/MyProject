package ru.zarina.zarina.domain.rework.common

data class Category(
    val id: Id,
    val code: Code,
    val name: String,
    val label: String?,
    val color: Color.Code?,
    val children: List<Category>?,
) {
    @JvmInline
    value class Id(val value: Long)

    @JvmInline
    value class Code(val value: String)
}

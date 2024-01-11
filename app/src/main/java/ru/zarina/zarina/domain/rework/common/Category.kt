package ru.zarina.zarina.domain.rework.common

data class Category(
    val id: Id,
) {
    @JvmInline
    value class Id(val value: Long)
}

package ru.livetyping.zarina.core.domain.model.category

public data class Category(
    val id: Id,
) {
    @JvmInline
    public value class Id(public val value: String)
}

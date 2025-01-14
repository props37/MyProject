package ru.livetyping.zarina.core.domain.model.user

// TODO: [High] Add to stability config

public data class MyCard(
    val number: Number,
) {
    @JvmInline
    public value class Number(public val value: String)
}

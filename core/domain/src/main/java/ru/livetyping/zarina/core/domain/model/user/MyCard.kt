package ru.livetyping.zarina.core.domain.model.user

// Marked as stable on config/compose/stability_config.txt
public data class MyCard(
    val number: Number,
) {
    // Marked as stable on config/compose/stability_config.txt
    @JvmInline
    public value class Number(public val value: String)
}

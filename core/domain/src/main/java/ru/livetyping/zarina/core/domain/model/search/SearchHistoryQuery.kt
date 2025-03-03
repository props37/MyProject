package ru.livetyping.zarina.core.domain.model.search

// Marked as stable on config/compose/stability_config.txt
public data class SearchHistoryQuery(
    val text: String,
    val timestampMillis: Long,
)

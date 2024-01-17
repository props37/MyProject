package ru.zarina.zarina.domain.rework.common

data class Media(
    val url: Url,
    val type: Type,
) {
    enum class Type { IMAGE, VIDEO }
}

package ru.zarina.zarina.domain.rework.common

data class Media(
    val url: Url,
) {
    enum class Type { IMAGE, VIDEO }
}

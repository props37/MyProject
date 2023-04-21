package ru.zarina.zarina.domain

data class Media(
    val url: Url,
    val type: Type,
) {
    enum class Type { IMAGE, VIDEO }
}

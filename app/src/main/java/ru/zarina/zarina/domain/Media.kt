package ru.zarina.zarina.domain

data class Media(
    val url: String,
    val type: Type,
) {
    enum class Type { IMAGE, VIDEO }
}

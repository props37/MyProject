package ru.zarina.zarina.domain

data class Media(
    val url: Url,
    val type: Type,
) {
    enum class Type { IMAGE, VIDEO }

    object Defaults {
        const val PRODUCT_MEDIA_ASPECT_RATIO = 3f / 4f
    }
}

package ru.zarina.zarina.domain.old

data class Media(
    val url: Url,
    val type: Type,
) {
    enum class Type { IMAGE, VIDEO }

    object Defaults {
        const val BANNER_MEDIA_ASPECT_RATIO = 1178f / 1623f
        const val PRODUCT_MEDIA_ASPECT_RATIO = 3f / 4f
        const val CATEGORY_MEDIA_ASPECT_RATIO = 69f / 28f
    }
}

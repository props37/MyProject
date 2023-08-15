package ru.zarina.zarina.domain

sealed interface RecommendationType {
    data class Similar(val product: Product) : RecommendationType
    object User : RecommendationType
}

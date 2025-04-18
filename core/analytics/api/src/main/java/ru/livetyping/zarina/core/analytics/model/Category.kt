package ru.livetyping.zarina.core.analytics.model

public data class Category(val name: String) {
    public companion object {
        public const val CATEGORY_WOMEN: String = "Женщинам"
        public const val CATEGORY_MEN: String = "Мужчинам"
    }
}

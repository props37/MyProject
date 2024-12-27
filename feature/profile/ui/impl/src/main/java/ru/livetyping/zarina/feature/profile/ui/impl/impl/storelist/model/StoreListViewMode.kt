package ru.livetyping.zarina.feature.profile.ui.impl.impl.storelist.model

internal enum class StoreListViewMode {
    MAP, LIST;

    companion object {
        fun getAll(): List<StoreListViewMode> = listOf(MAP, LIST)
    }
}

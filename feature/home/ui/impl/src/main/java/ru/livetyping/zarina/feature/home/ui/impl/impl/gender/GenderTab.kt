package ru.livetyping.zarina.feature.home.ui.impl.impl.gender

internal enum class GenderTab {
    WOMEN,
    MEN;

    companion object {
        fun getTabs(): List<GenderTab> {
            return entries.toList()
        }
    }
}

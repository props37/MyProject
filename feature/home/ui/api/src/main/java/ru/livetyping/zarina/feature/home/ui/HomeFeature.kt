package ru.livetyping.zarina.feature.home.ui

import ru.livetyping.zarina.core.feature.SingleFeatureEntry

public interface HomeFeature : SingleFeatureEntry<HomeNavEntry, Unit, HomeNavActions> {
    public companion object {
        public val NavEntry: HomeNavEntry = HomeNavEntry
    }
}

package ru.livetyping.zarina.feature.home.ui

import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import kotlin.reflect.KClass

public interface HomeFeature : ComposableFeatureEntry<HomeNavEntry, Unit, HomeNavActions> {
    public companion object {
        public fun getNavEntry(): HomeNavEntry = HomeNavEntry

        public fun getNavEntryClass(): KClass<HomeNavEntry> = HomeNavEntry::class
    }
}

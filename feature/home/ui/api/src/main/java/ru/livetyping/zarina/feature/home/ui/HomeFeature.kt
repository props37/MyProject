package ru.livetyping.zarina.feature.home.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.NavigationEntry
import kotlin.reflect.KClass

public interface HomeFeature : ComplexFeatureEntry<HomeNavEntry, HomeNavActions, Unit> {
    public companion object {
        public fun getNavEntry(): HomeNavEntry = HomeNavEntry

        public fun getNavEntryClass(): KClass<HomeNavEntry> = HomeNavEntry::class

        public fun getInitialScreenNavEntry(): NavigationEntry = HomeScreenNavEntry
    }
}

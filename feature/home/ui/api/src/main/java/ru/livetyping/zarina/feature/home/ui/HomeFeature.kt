package ru.livetyping.zarina.feature.home.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationEntry
import kotlin.reflect.KClass

public interface HomeFeature :
    ComplexFeatureEntry<HomeNavEntry, HomeNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(): HomeNavEntry = HomeNavEntry

        public fun getNavEntryClass(): KClass<HomeNavEntry> = HomeNavEntry::class

        public fun getStartNavEntry(): NavigationEntry = HomeNavEntry.StartNavEntry
    }
}

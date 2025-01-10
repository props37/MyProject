package ru.livetyping.zarina.feature.profile.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.NavigationEntry
import kotlin.reflect.KClass

public interface ProfileFeature :
    ComplexFeatureEntry<ProfileNavEntry, ProfileNavActions, ProfileNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(): ProfileNavEntry = ProfileNavEntry

        public fun getNavEntryClass(): KClass<ProfileNavEntry> = ProfileNavEntry::class

        public fun getStartNavEntry(): NavigationEntry = ProfileNavEntry.StartNavEntry
    }
}

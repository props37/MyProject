package ru.livetyping.zarina.feature.profile.ui

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import kotlin.reflect.KClass

public interface ProfileFeature :
    ComplexFeatureEntry<ProfileNavEntry, ProfileNavActions, ProfileNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(): ProfileNavEntry = ProfileNavEntry

        public fun getNavEntryClass(): KClass<ProfileNavEntry> = ProfileNavEntry::class
    }
}

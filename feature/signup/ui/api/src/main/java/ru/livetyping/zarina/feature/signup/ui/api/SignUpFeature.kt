package ru.livetyping.zarina.feature.signup.ui.api

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import kotlin.reflect.KClass

public interface SignUpFeature : ComplexFeatureEntry<SignUpNavEntry, Unit, SignUpNavActions> {
    public companion object {
        public fun getNavEntry(): SignUpNavEntry = SignUpNavEntry

        public fun getNavEntryClass(): KClass<SignUpNavEntry> = SignUpNavEntry::class
    }
}

package ru.livetyping.zarina.feature.signin.ui.api

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import kotlin.reflect.KClass

public interface SignInFeature :
    ComplexFeatureEntry<SignInNavEntry, SignInNavActions, EmptyNavResultRetrievers> {

    public companion object {
        public fun getNavEntry(): SignInNavEntry = SignInNavEntry

        public fun getNavEntryClass(): KClass<SignInNavEntry> = SignInNavEntry::class
    }
}

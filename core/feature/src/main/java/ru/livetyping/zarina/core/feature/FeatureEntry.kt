package ru.livetyping.zarina.core.feature

import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigation.NavigationParams
import kotlin.reflect.KClass

public interface FeatureEntry<
        NavEntry : NavigationEntry,
        NavParams : NavigationParams,
        NavActions : NavigationActions> {

    public fun getNavEntry(params: NavParams): NavEntry

    public fun getNavEntryClass(): KClass<NavEntry>
}

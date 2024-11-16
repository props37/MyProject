package ru.livetyping.zarina.core.feature

import kotlin.reflect.KClass

public interface FeatureEntry<NavEntry : Any, NavParams, NavActions> {
    public fun getNavEntry(params: NavParams): NavEntry

    public fun getNavEntryClass(): KClass<NavEntry>
}

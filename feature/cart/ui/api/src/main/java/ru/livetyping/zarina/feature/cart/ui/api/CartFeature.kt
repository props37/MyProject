package ru.livetyping.zarina.feature.cart.ui.api

import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.NavigationEntry
import kotlin.reflect.KClass

public interface CartFeature : ComplexFeatureEntry<CartNavEntry, CartNavActions, Unit> {
    public companion object {
        public fun getNavEntry(): CartNavEntry = CartNavEntry

        public fun getNavEntryClass(): KClass<CartNavEntry> = CartNavEntry::class

        public fun getStartNavEntry(): NavigationEntry = CartNavEntry.StartNavEntry
    }
}

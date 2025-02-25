package ru.livetyping.zarina.feature.search.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature.NavActions
import ru.livetyping.zarina.feature.search.ui.api.SearchFeature.NavEntry

public interface SearchFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry

    public class NavActions : NavigationActions
}

package ru.livetyping.zarina.feature.signup.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature.NavActions
import ru.livetyping.zarina.feature.signup.ui.api.SignUpFeature.NavEntry

public interface SignUpFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry

    public object NavActions : NavigationActions
}

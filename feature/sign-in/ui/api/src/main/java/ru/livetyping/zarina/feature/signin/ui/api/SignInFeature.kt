package ru.livetyping.zarina.feature.signin.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature.NavActions
import ru.livetyping.zarina.feature.signin.ui.api.SignInFeature.NavEntry

public interface SignInFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry

    public class NavActions(
        public val onSignUpClicked: () -> Unit,
    ) : NavigationActions
}

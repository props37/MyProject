package ru.livetyping.zarina.feature.webview.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature.NavActions
import ru.livetyping.zarina.feature.webview.ui.WebViewFeature.NavEntry

public interface WebViewFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data class NavEntry(val url: String) : NavigationEntry

    public class NavActions(
        public val onBackClicked: () -> Unit,
    ) : NavigationActions
}

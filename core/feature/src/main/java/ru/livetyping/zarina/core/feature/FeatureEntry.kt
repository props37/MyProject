package ru.livetyping.zarina.core.feature

import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigation.NavigationResultRetrievers

public interface FeatureEntry<
        NavEntry : NavigationEntry,
        NavActions : NavigationActions,
        NavResultRetrievers : NavigationResultRetrievers>

package ru.livetyping.zarina.feature.home.ui

import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.feature.home.domain.model.Banner

public class HomeNavActions(
    public val onBannerClicked: (Banner) -> Unit,
) : NavigationActions

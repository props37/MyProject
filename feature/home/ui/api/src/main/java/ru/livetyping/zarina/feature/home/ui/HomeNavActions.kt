package ru.livetyping.zarina.feature.home.ui

import ru.livetyping.zarina.feature.home.domain.model.Banner

public class HomeNavActions(
    public val bannerClicked: (Banner) -> Unit,
)

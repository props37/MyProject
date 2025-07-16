package ru.livetyping.zarina.feature.profile.ui.impl.order

import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.navigation.NavigationActions

internal class OrderNavActions(
    val onBackClicked: () -> Unit,
    val onPayClicked: (Url) -> Unit,
) : NavigationActions

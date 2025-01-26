package ru.livetyping.zarina.feature.productsubscription.ui.api

import ru.livetyping.zarina.core.navigation.NavigationActions

public class ProductSubscriptionNavActions(
    public val onBackClicked: () -> Unit,
    public val onSubscriptionCompleted: () -> Unit,
) : NavigationActions

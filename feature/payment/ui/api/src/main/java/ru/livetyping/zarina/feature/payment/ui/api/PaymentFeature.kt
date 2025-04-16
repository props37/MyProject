package ru.livetyping.zarina.feature.payment.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature.NavActions
import ru.livetyping.zarina.feature.payment.ui.api.PaymentFeature.NavEntry

public interface PaymentFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry(public val paymentUrl: String) : NavigationEntry

    public class NavActions(
        public val onBackClicked: () -> Unit,
    ) : NavigationActions
}

package ru.livetyping.zarina.feature.cart.ui.api

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.cart.CartProduct
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigation.NavigationResultRetrievers
import ru.livetyping.zarina.core.navigationutil.ScreenResultRetriever
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature.NavActions
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature.NavEntry
import ru.livetyping.zarina.feature.cart.ui.api.CartFeature.NavResultRetrievers

public interface CartFeature :
    ComplexFeatureEntry<NavEntry, NavActions, NavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry {

        @Serializable
        public data object StartNavEntry : NavigationEntry
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onChangeCityClicked: (City?) -> Unit,
        public val onGoToCatalogClicked: () -> Unit,
        public val onProductClicked: (CartProduct) -> Unit,
    ) : NavigationActions

    public class NavResultRetrievers(
        public val selectedCityResultRetriever: ScreenResultRetriever<CartSelectedCityResult>,
    ) : NavigationResultRetrievers
}

package ru.livetyping.zarina.feature.catalog.ui

import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.category.Category
import ru.livetyping.zarina.core.domain.model.common.Url
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComplexFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature.NavActions
import ru.livetyping.zarina.feature.catalog.ui.CatalogFeature.NavEntry

public interface CatalogFeature :
    ComplexFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data object NavEntry : NavigationEntry {

        @Serializable
        public data object StartNavEntry : NavigationEntry
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onSearchClicked: () -> Unit,
        public val onCategoryClicked: (Category.Id) -> Unit,
        public val onUrlClicked: (Url) -> Unit,
        public val onCityClicked: (City) -> Unit,
    ) : NavigationActions
}

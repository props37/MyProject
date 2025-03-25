package ru.livetyping.zarina.feature.cityselector.ui

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.parcelableNavType
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature.NavActions
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature.NavEntry
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public interface CitySelectorFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public class NavEntry private constructor(
        public val title: Text?,
        public val currentCity: CityParcelable?,
    ) : NavigationEntry {
        public companion object {
            public fun create(
                title: Text? = null,
                currentCity: City? = null,
            ): NavEntry {
                return NavEntry(
                    title = title,
                    currentCity = currentCity?.let { CityParcelable.from(it) },
                )
            }

            public fun typeMap(): Map<KType, NavType<*>> {
                val textType = parcelableNavType<Text?>(
                    isNullableAllowed = true,
                )
                val cityType = parcelableNavType<CityParcelable?>(
                    isNullableAllowed = true,
                )
                return mapOf(
                    typeOf<Text?>() to textType,
                    typeOf<CityParcelable?>() to cityType,
                )
            }
        }
    }

    public class NavActions(
        public val onBackClicked: () -> Unit,
        public val onCitySelected: (City) -> Unit,
    ) : NavigationActions
}

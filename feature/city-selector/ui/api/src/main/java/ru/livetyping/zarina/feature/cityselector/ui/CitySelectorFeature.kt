package ru.livetyping.zarina.feature.cityselector.ui

import androidx.navigation.NavType
import kotlinx.serialization.Serializable
import ru.livetyping.zarina.core.domain.model.geo.City
import ru.livetyping.zarina.core.feature.ComposableFeatureEntry
import ru.livetyping.zarina.core.navigation.EmptyNavResultRetrievers
import ru.livetyping.zarina.core.navigation.NavigationActions
import ru.livetyping.zarina.core.navigation.NavigationEntry
import ru.livetyping.zarina.core.navigationutil.ParcelableNavType
import ru.livetyping.zarina.core.text.Text
import ru.livetyping.zarina.core.uimodel.geo.CityParcelable
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature.NavActions
import ru.livetyping.zarina.feature.cityselector.ui.CitySelectorFeature.NavEntry
import kotlin.reflect.KClass
import kotlin.reflect.KType
import kotlin.reflect.typeOf

public interface CitySelectorFeature :
    ComposableFeatureEntry<NavEntry, NavActions, EmptyNavResultRetrievers> {

    @Serializable
    public data class NavEntry(
        val title: Text? = null,
        val currentCity: CityParcelable? = null,
    ) : NavigationEntry {
        public companion object {
            public fun typeMap(): Map<KType, NavType<*>> {
                val textType = ParcelableNavType<Text?>(
                    isNullableAllowed = true,
                    serializer = kotlinx.serialization.serializer(),
                )
                val cityType = ParcelableNavType<CityParcelable?>(
                    isNullableAllowed = true,
                    serializer = kotlinx.serialization.serializer(),
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

    public data class NavParams(
        val title: Text? = null,
        val currentCity: City? = null,
    ) {
        public fun toNavEntry(): NavEntry {
            return NavEntry(
                title = title,
                currentCity = currentCity?.let { CityParcelable.from(it) },
            )
        }
    }

    public companion object {
        public fun getNavEntry(params: NavParams): NavEntry {
            return params.toNavEntry()
        }

        public fun getNavEntryClass(): KClass<NavEntry> = NavEntry::class
    }
}
